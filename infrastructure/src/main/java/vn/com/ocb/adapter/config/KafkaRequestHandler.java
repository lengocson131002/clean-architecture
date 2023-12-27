package vn.com.ocb.adapter.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import vn.com.ocb.usecase.user.v2.model.GetAllUserRequestV2;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Slf4j
public class KafkaRequestHandler implements Runnable {

    private final KafkaConsumer<String, KafkaMessage<GetAllUserRequestV2>> consumer;

    @Inject
    public KafkaRequestHandler() {
        Properties props = new Properties();
        props.put("client.id", UUID.randomUUID().toString());
        props.put("group.id", KafkaConfig.SERVICE_NAME);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", KafkaDeserializer.class.getName());
        props.put("bootstrap.servers", "localhost:9092");
        consumer = new KafkaConsumer<>(props);
    }

    @Override
    public void run() {
        consumer.subscribe(List.of(KafkaConfig.KAFKA_TOPIC_REQUEST));
        while (true) {
            ConsumerRecords<String, KafkaMessage<GetAllUserRequestV2>> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, KafkaMessage<GetAllUserRequestV2>> record : records) {
                log.info("Received request: {}", record.value());
            }
        }
    }

}
