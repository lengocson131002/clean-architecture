package vn.com.ocb.pipeline.request.impl.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.pipeline.request.Request;
import vn.com.ocb.pipeline.request.RequestHandler;
import vn.com.ocb.pipeline.request.RequestPipeline;

import java.nio.charset.StandardCharsets;
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
        threadPool = Executors.newFixedThreadPool(50);
    }

    @Override
    public <TRequest extends Request<TResponse>, TResponse> void onHandlerRegistered(
            Class<TRequest> requestType,
            Class<TResponse> responseType,
            RequestHandler<TRequest, TResponse> handler) {
        if (!notifiers.containsKey(requestType)) {
            KafkaNotifier<TRequest, TResponse> kafkaRequestNotifier = new KafkaNotifier<>(requestType, responseType);
            notifiers.put(requestType, kafkaRequestNotifier);
        }
        threadPool.submit(() -> consumingRequest(requestType));
    }

    @Override
    public <TRequest extends Request<TResponse>, TResponse> CompletableFuture<TResponse> handleRequest(TRequest request, RequestHandler<TRequest, TResponse> handler) {
        KafkaNotifier<TRequest, TResponse> kafkaNotifier = getKafkaNotifier(request);
        return kafkaNotifier.send(request);
    }

    @SuppressWarnings("unchecked")
    private <TResponse, TRequest extends Request<TResponse>> KafkaNotifier<TRequest, TResponse> getKafkaNotifier(TRequest request) {
        return (KafkaNotifier<TRequest, TResponse>) notifiers.get(request.getClass());
    }

    @SuppressWarnings("unchecked")
    private <TRequest extends Request<TResponse>, TResponse> void consumingRequest(Class<TRequest> requestType) {
        final KafkaNotifier<TRequest, TResponse> notifier = (KafkaNotifier<TRequest, TResponse>) notifiers.get(requestType);
        final RequestHandler<TRequest, TResponse> requestHandler = getHandler(requestType);
        try (KafkaConsumer<String, KafkaRequestMessage<TRequest>> kafkaRequestConsumer = new KafkaConsumer<>(
                KafkaConfig.getConsumerProps(),
                new StringDeserializer(),
                new KafkaDeserializer<>(notifier.getRequestMessageTypeReference()));
             KafkaProducer<String, KafkaResponseMessage<TResponse>> responseKafkaProducer = new KafkaProducer<>(
                     KafkaConfig.getProducerProps(),
                     new StringSerializer(),
                     new KafkaSerializer<>()
             )) {
            kafkaRequestConsumer.subscribe(List.of(notifier.getRequestTopic()));
            while (running) {
                ConsumerRecords<String, KafkaRequestMessage<TRequest>> records = kafkaRequestConsumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, KafkaRequestMessage<TRequest>> record : records) {
                    Header requestIdHeader = record.headers().lastHeader(KafkaConfig.HEADER_REQUEST_ID);
                    String requestId = new String(requestIdHeader.value(), StandardCharsets.UTF_8);
                    TRequest request = record.value().getData();
                    log.info("Received message: Topic: {}, Value: {}", record.topic(), request);

                    KafkaResponseMessage<TResponse> kafkaResponseMessage = null;
                    try {
                        TResponse response = requestHandler.handle(request);
                        kafkaResponseMessage = KafkaResponseMessage.<TResponse>builder()
                                .requestId(requestId)
                                .data(response)
                                .build();
                    } catch (Exception ex) {
                        log.error("🔥 Error when processing message. Request ID: {}", requestId);
                        if (ex.getCause() instanceof CoreException) {
                            CoreException exception = (CoreException) ex.getCause();
                            kafkaResponseMessage = KafkaResponseMessage.<TResponse>builder()
                                    .requestId(requestId)
                                    .errorCode(exception.getCode())
                                    .error(exception.getError())
                                    .errorMessage(exception.getMessage())
                                    .build();
                        } else {
                            kafkaResponseMessage = KafkaResponseMessage.<TResponse>builder()
                                    .requestId(requestId)
                                    .errorCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                                    .error(ResponseCode.INTERNAL_SERVER_ERROR.toString())
                                    .errorMessage(ex.getMessage())
                                    .build();
                        }
                    }

                    final ProducerRecord<String, KafkaResponseMessage<TResponse>> responseRecord = new ProducerRecord<>(notifier.getResponseTopic(), requestId, kafkaResponseMessage);
                    responseRecord.headers().add(new RecordHeader(KafkaConfig.HEADER_REQUEST_ID, requestIdHeader.value()));
                    responseKafkaProducer.send(responseRecord);
                }
            }
        }
    }
}
