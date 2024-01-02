package vn.com.ocb.pipeline.request.impl.kafka;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaResponseMessage<T> {
    private String requestId;
    private String errorCode;
    private String error;
    private String errorMessage;
    private T data;

    public boolean isSuccess() {
        return errorCode == null;
    }
}
