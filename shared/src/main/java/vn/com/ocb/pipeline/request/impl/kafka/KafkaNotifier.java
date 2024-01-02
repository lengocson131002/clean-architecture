package vn.com.ocb.pipeline.request.impl.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.pipeline.request.Request;
import vn.com.ocb.pipeline.request.RequestTimeoutException;

import java.lang.reflect.Type;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class KafkaNotifier<TRequest extends Request<TResponse>, TResponse> {
    private boolean _running = true;
    private final String requestTopic;
    private final String responseTopic;
    private final ConcurrentMap<String, CompletableFuture<TResponse>> futures;
    private final KafkaProducer<String, KafkaRequestMessage<TRequest>> requestProducer;
    private final KafkaConsumer<String, KafkaResponseMessage<TResponse>> responseConsumer;
    private final ScheduledExecutorService scheduledExecutorService;
    private final TypeReference<KafkaRequestMessage<TRequest>> requestMessageTypeReference;
    private final TypeReference<KafkaResponseMessage<TResponse>> responseMessageTypeReference;

    public KafkaNotifier(Class<TRequest> requestClass, Class<TResponse> responseClass) {
        final ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        this.requestMessageTypeReference = new TypeReference<>() {
            @Override
            public Type getType() {
                return typeFactory.constructParametricType(KafkaRequestMessage.class, requestClass);
            }
        };

        this.responseMessageTypeReference = new TypeReference<>() {
            @Override
            public Type getType() {
                return typeFactory.constructParametricType(KafkaResponseMessage.class, responseClass);
            }
        };

        futures = new ConcurrentHashMap<>();
        requestTopic = "request-" + requestClass.getName();
        responseTopic = "response-" + requestClass.getName();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        requestProducer = new KafkaProducer<>(
                KafkaConfig.getProducerProps(),
                new StringSerializer(),
                new KafkaSerializer<>());

        responseConsumer = new KafkaConsumer<>(
                KafkaConfig.getConsumerProps(),
                new StringDeserializer(),
                new KafkaDeserializer<>(this.responseMessageTypeReference));

//        Consume response in separate thread
        ExecutorService threadPool = Executors.newCachedThreadPool();
        threadPool.submit(this::consumingResponses);
        threadPool.shutdown();
    }

    public TypeReference<KafkaRequestMessage<TRequest>> getRequestMessageTypeReference() {
        return requestMessageTypeReference;
    }

    public TypeReference<KafkaResponseMessage<TResponse>> getResponseMessageTypeReference() {
        return responseMessageTypeReference;
    }

    public String getRequestTopic() {
        return this.requestTopic;
    }

    public String getResponseTopic() {
        return this.responseTopic;
    }

    public CompletableFuture<TResponse> send(final TRequest request) {
        KafkaRequestMessage<TRequest> kafkaRequestMessage = KafkaMessageUtils.createRequestMessage(request.getRequestId(), request);
        return sendKafkaRequestMessage(kafkaRequestMessage);
    }

    private CompletableFuture<TResponse> sendKafkaRequestMessage(KafkaRequestMessage<TRequest> message) {
        CompletableFuture<TResponse> future = new CompletableFuture<>();
        try {
            final ProducerRecord<String, KafkaRequestMessage<TRequest>> record = new ProducerRecord<>(requestTopic, message.getRequestId(), message);
            requestProducer.send(record, (res, e) -> {
                boolean isError = (e != null);
                if (!isError) {
                    futures.put(message.getRequestId(), future);
                    log.info("[Kafka request] Send request successfully {}", record);
                    scheduleTimeout(message.getRequestId(), KafkaConfig.KAFKA_PIPELINE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                } else {
                    log.error("[Kafka request] Send failed for record {}", record, e);
                    future.completeExceptionally(e);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Failed to send request to Kafka broker", ex);
        }
        return future;
    }


    private void consumingResponses() {
        responseConsumer.subscribe(List.of(getResponseTopic()));
        while (_running) {
            ConsumerRecords<String, KafkaResponseMessage<TResponse>> records = responseConsumer.poll(Duration.ofMillis(100));
            onMessage(records);
        }
    }

    private void onMessage(ConsumerRecords<String, KafkaResponseMessage<TResponse>> records) {
        records.forEach(record -> {
            try {
                if (record.value() == null) {
                    return;
                }
                KafkaResponseMessage<TResponse> kafkaResponseMessage = record.value();
                String requestId = kafkaResponseMessage.getRequestId();
                CompletableFuture<TResponse> future = futures.get(requestId);
                if (future != null) {
                    if (!future.isDone()) {
                        log.info("[Kafka Response Consumer] Consumed response message {}", kafkaResponseMessage);
                        if (kafkaResponseMessage.isSuccess()) {
                            future.complete(kafkaResponseMessage.getData());
                        } else {
                            future.completeExceptionally(new CoreException(kafkaResponseMessage.getErrorCode(), kafkaResponseMessage.getError(), kafkaResponseMessage.getErrorMessage()));
                        }
                    }
                    futures.remove(requestId);
                }
            } catch (Exception ex) {
                log.error("Error when handle consuming record {}", record);
            }
        });
    }

    private void scheduleTimeout(String requestId, long ms, TimeUnit unit) {
        this.scheduledExecutorService.schedule(() -> {
            CompletableFuture<TResponse> removed = this.futures.remove(requestId);
            if (removed != null) {
                log.warn("Timeout request {}", requestId);
                removed.completeExceptionally(new RequestTimeoutException(requestId));
            }
        }, ms, unit);
    }
}
