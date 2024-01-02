package vn.com.ocb.pipeline.request;

public class RequestTimeoutException extends RuntimeException {

    private final String message;

    public RequestTimeoutException(String requestId) {
        message = String.format("Request timeout. Request id: ", requestId);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
