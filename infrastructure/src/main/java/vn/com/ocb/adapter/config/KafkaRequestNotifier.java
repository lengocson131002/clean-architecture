package vn.com.ocb.adapter.config;

import an.awesome.pipelinr.Pipelinr;
import an.awesome.pipelinr.StreamSupplier;
import an.awesome.pipelinr.repack.org.checkerframework.checker.units.qual.K;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import vn.com.ocb.config.Request;
import vn.com.ocb.config.RequestHandler;
import vn.com.ocb.config.RequestNotifier;
import vn.com.ocb.config.RequestTimeoutException;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Slf4j
public class KafkaRequestNotifier implements RequestNotifier {
    private final ScheduledExecutorService executorService;
    private final ConcurrentMap<Object, CompletableFuture<?>> futures;
    private final String REQUEST_TOPIC_PREFIX = "request-";
    private final String RESPONSE_TOPIC_PREFIX = "response-";
    private List<RequestHandler> handlers = Stream::empty;
    private boolean running = true;

    public KafkaRequestNotifier() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        futures = new ConcurrentHashMap<>();
    }

    public KafkaRequestNotifier with() {
        this.notificationHandlingStrategySupplier = notificationHandlingStrategySupplier;
        return this;
    }


    private void consuming() {
        kafkaResponseConsumer.subscribe(List.of(responseTopic));
        while (running) {
            ConsumerRecords<String, TResponse> records = kafkaResponseConsumer.poll(Duration.ofMillis(100));
            onMessage(records);
        }
    }

    private Properties getProducerProps() {
        Properties props = new Properties();
        props.put("client.id", UUID.randomUUID().toString());
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", KafkaSerializer.class.getName());
        props.put("acks", "all");
        return props;
    }

    private Properties getConsumerProps() {
        Properties props = new Properties();
        props.put("client.id", UUID.randomUUID().toString());
        props.put("group.id", KafkaConfig.SERVICE_NAME);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", KafkaDeserializer.class.getName());
        props.put("bootstrap.servers", "localhost:9092");
        return props;
    }

    @Override
    public <TRequest extends Request, TResponse> CompletableFuture<TResponse> send(TRequest request) {
        String requestType = request.getClass().getSimpleName();
        String requestId = UUID.randomUUID().toString();
        byte[] requestIdInBytes = requestId.getBytes(StandardCharsets.UTF_8);
        CompletableFuture<TResponse> future = new CompletableFuture<>();
        try (KafkaProducer<String, TRequest> producer = new KafkaProducer<>(getProducerProps())) {
            final ProducerRecord<String, TRequest> record = new ProducerRecord<>(requestTopic, requestType, request);
            record.headers().add(new RecordHeader(KafkaConfig.HEADER_REQUEST_ID, requestIdInBytes));
            producer.send(record, (res, e) -> {
                boolean isError = (e != null);
                if (!isError) {
                    log.info("[Kafka request] Send request successfully {}", record);
                    futures.put(requestId, future);
                    scheduleTimeout(requestId, KafkaConfig.KAFKA_PIPELINE_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                    consuming();
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

    private void onMessage(ConsumerRecords<String, TResponse> records) {
        records.forEach(record -> {
            try {
                Header requestIdHeader = record.headers().lastHeader(KafkaConfig.HEADER_REQUEST_ID);
                if (requestIdHeader == null) {
                    log.warn("Missing request header for received record");
                } else {
                    CompletableFuture<TResponse> future = futures.remove(new String(requestIdHeader.value(), StandardCharsets.UTF_8));
                    future.complete(record.value());
                }
            } catch (Exception ex) {
                log.error("Error when handle consuming record {}", record);
            }
        });
    }

    private void scheduleTimeout(String requestId, long ms, TimeUnit unit) {
        this.executorService.schedule(() -> {
            CompletableFuture<TResponse> removed = this.futures.remove(requestId);
            if (removed != null) {
                log.warn("Timeout request {}", requestId);
                removed.completeExceptionally(new RequestTimeoutException(requestId));
            }
        }, ms, unit);
    }

}
