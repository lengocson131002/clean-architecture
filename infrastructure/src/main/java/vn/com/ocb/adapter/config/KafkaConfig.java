package vn.com.ocb.adapter.config;

public class KafkaConfig {
    public static final String SERVICE_NAME = "service-user";
    public static final long KAFKA_PIPELINE_TIMEOUT_MS = 5_000L; //
    public static final String KAFKA_TOPIC_REQUEST = SERVICE_NAME + "_request";
    public static final String HEADER_REQUEST_ID = "requestId";
}
