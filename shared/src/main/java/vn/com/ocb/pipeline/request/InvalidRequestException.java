package vn.com.ocb.pipeline.request;

public class InvalidRequestException extends RuntimeException {
    private final String message;

    public InvalidRequestException(Request<?> request) {
        message = String.format("Invalidated request. Request type: %s", request.getClass().getSimpleName());
    }

    @Override
    public String getMessage() {
        return message;
    }
}
