package vn.com.ocb.pipeline.request.impl.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ocb.pipeline.request.Request;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaRequestMessage<TRequest extends Request<?>> {
    private String requestId;
    private TRequest data;
    private long timestamp;

    public KafkaRequestMessage(String requestId, TRequest data) {
        this.requestId = requestId;
        this.data = data;
        this.timestamp = Instant.now().getEpochSecond();
    }
}
