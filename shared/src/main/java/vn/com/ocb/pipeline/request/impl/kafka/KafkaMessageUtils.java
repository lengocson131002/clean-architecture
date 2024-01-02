package vn.com.ocb.pipeline.request.impl.kafka;

import vn.com.ocb.exception.CoreException;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.pipeline.request.Request;

public class KafkaMessageUtils {
    public static <T extends Request<?>> KafkaRequestMessage<T> createRequestMessage(String requestId, T requestData) {
        return KafkaRequestMessage.<T>builder()
                .requestId(requestId)
                .data(requestData)
                .build();
    }

    public static <T> KafkaResponseMessage<T> createResponseMessage(String requestId, T responseData) {
        return KafkaResponseMessage.<T>builder()
                .requestId(requestId)
                .data(responseData)
                .build();
    }

    public static <T> KafkaResponseMessage<T> createErrorResponseMessage(String requestId, Throwable throwable) {
        if (throwable.getCause() instanceof CoreException) {
            CoreException exception = (CoreException) throwable.getCause();
            return KafkaResponseMessage.<T>builder()
                    .requestId(requestId)
                    .errorCode(exception.getCode())
                    .error(exception.getError())
                    .errorMessage(exception.getMessage())
                    .build();
        }
        return KafkaResponseMessage.<T>builder()
                .requestId(requestId)
                .errorCode(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                .error(ResponseCode.INTERNAL_SERVER_ERROR.toString())
                .errorMessage(throwable.getMessage())
                .build();
    }
}
