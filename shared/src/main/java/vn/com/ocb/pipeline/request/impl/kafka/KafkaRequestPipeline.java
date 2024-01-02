package vn.com.ocb.pipeline.request.impl.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import vn.com.ocb.pipeline.request.Request;
import vn.com.ocb.pipeline.request.RequestHandler;
import vn.com.ocb.pipeline.request.RequestPipeline;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class KafkaRequestPipeline extends RequestPipeline {
    private boolean running = true;
    private final ConcurrentMap<Class<? extends Request<?>>, KafkaNotifier<?, ?>> notifiers;
    private final ExecutorService threadPool;

    public KafkaRequestPipeline() {
        notifiers = new ConcurrentHashMap<>();
        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public <TRequest extends Request<TResponse>, TResponse> void onHandlerRegistered(Class<TRequest> requestClass, Class<TResponse> responseClass, RequestHandler<TRequest, TResponse> handler) {
        if (!notifiers.containsKey(requestClass)) {
            KafkaNotifier<TRequest, TResponse> kafkaRequestNotifier = new KafkaNotifier<>(requestClass, responseClass);
            notifiers.put(requestClass, kafkaRequestNotifier);
        }
        threadPool.submit(() -> consumingRequest(requestClass));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <TRequest extends Request<TResponse>, TResponse> CompletableFuture<TResponse> handleRequest(TRequest request, RequestHandler<TRequest, TResponse> handler) {
        KafkaNotifier<TRequest, TResponse> kafkaNotifier = getKafkaNotifier(request.getClass());
        return kafkaNotifier.send(request);
    }

    @SuppressWarnings("unchecked")
    private <TResponse, TRequest extends Request<TResponse>> KafkaNotifier<TRequest, TResponse> getKafkaNotifier(Class<TRequest> requestType) {
        if (!notifiers.containsKey(requestType)) {
            throw new IllegalArgumentException(String.format("There is no kafka notifier available for request type: %s", requestType.getName()));
        }
        return (KafkaNotifier<TRequest, TResponse>) notifiers.get(requestType);
    }

    @SuppressWarnings("unchecked")
    private <TRequest extends Request<TResponse>, TResponse> void consumingRequest(Class<TRequest> requestType) {
        final KafkaNotifier<TRequest, TResponse> notifier = getKafkaNotifier(requestType);
        final RequestHandler<TRequest, TResponse> requestHandler = getHandler(requestType);
        try (KafkaConsumer<String, KafkaRequestMessage<TRequest>> kafkaRequestConsumer = new KafkaConsumer<>(KafkaConfig.getConsumerProps(), new StringDeserializer(), new KafkaDeserializer<>(notifier.getRequestMessageTypeReference())); KafkaProducer<String, KafkaResponseMessage<TResponse>> kafkaResponseProducer = new KafkaProducer<>(KafkaConfig.getProducerProps(), new StringSerializer(), new KafkaSerializer<>())) {
            kafkaRequestConsumer.subscribe(List.of(notifier.getRequestTopic()));
            while (running) {
                ConsumerRecords<String, KafkaRequestMessage<TRequest>> records = kafkaRequestConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, KafkaRequestMessage<TRequest>> record : records) {
                    KafkaRequestMessage<TRequest> kafkaRequestMessage = record.value();
                    if (kafkaRequestMessage == null) {
                        continue;
                    }

                    String requestId = kafkaRequestMessage.getRequestId();
                    TRequest requestData = record.value().getData();
                    log.info("Received request message: Topic: {}, Value: {}", record.topic(), requestData);

                    KafkaResponseMessage<TResponse> kafkaResponseMessage = null;
                    try {
                        TResponse response = requestHandler.handle(requestData);
                        kafkaResponseMessage = KafkaMessageUtils.createResponseMessage(requestId, response);
                    } catch (Exception ex) {
                        log.error("🔥 Error when processing message. Request ID: {}", requestId);
                        kafkaResponseMessage = KafkaMessageUtils.createErrorResponseMessage(requestId, ex);
                    }

                    final ProducerRecord<String, KafkaResponseMessage<TResponse>> responseRecord = new ProducerRecord<>(notifier.getResponseTopic(), requestId, kafkaResponseMessage);
                    kafkaResponseProducer.send(responseRecord);
                }
            }
        }
    }
}
