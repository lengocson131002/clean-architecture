package vn.com.ocb.pipeline.request.impl.kafka;

import java.util.Properties;

public class KafkaConfig {
    public static final String SERVICE_NAME = "service-user";
    public static final long KAFKA_PIPELINE_TIMEOUT_MS = 5_000L; //
    public static final String HEADER_REQUEST_ID = "requestId";

    public static Properties getProducerProps() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        return props;
    }

    public static Properties getConsumerProps() {
        Properties props = new Properties();
        props.put("group.id", KafkaConfig.SERVICE_NAME);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("bootstrap.servers", "localhost:9092");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        return props;
    }
}
