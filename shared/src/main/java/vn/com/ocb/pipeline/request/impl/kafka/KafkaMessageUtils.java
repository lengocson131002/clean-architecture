package vn.com.ocb.pipeline.request.impl.kafka;

import vn.com.ocb.exception.CoreException;
import vn.com.ocb.pipeline.request.Request;

public class KafkaMessageUtils {
    private static final String INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR";
    private static final String INTERNAL_SERVER_ERROR = "Internal server error";

    public static <T extends Request<?>> KafkaRequestMessage<T> createRequestMessage(String requestId, T requestData) {
        return new KafkaRequestMessage<>(requestId, requestData);
    }

    public static <T> KafkaResponseMessage<T> createResponseMessage(String requestId, T responseData) {
        return KafkaResponseMessage.<T>builder()
                .requestId(requestId)
                .data(responseData)
                .build();
    }

    public static <T> KafkaResponseMessage<T> createErrorResponseMessage(String requestId, Throwable throwable) {
        if (throwable instanceof CoreException) {
            CoreException exception = (CoreException) throwable;
            return KafkaResponseMessage.<T>builder()
                    .requestId(requestId)
                    .errorCode(exception.getCode())
                    .error(exception.getError())
                    .errorMessage(exception.getMessage())
                    .build();
        }
        return KafkaResponseMessage.<T>builder()
                .requestId(requestId)
                .errorCode(INTERNAL_SERVER_ERROR_CODE)
                .error(INTERNAL_SERVER_ERROR)
                .errorMessage(throwable.getMessage())
                .build();
    }
}
