package vn.com.ocb.pipeline.request.impl.kafka;

import lombok.Data;

import java.util.UUID;

@Data
public class KafkaMessage<TRequest> {
    private String requestId;
    private TRequest data;

    public KafkaMessage(TRequest data) {
        this.requestId = UUID.randomUUID().toString();
        this.data = data;
    }
}