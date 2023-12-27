package vn.com.ocb.config;

public class RequestTimeoutException extends RuntimeException {
    public RequestTimeoutException(String requestId) {
        super(String.format("Request timeout. Request ID: %s", requestId));
    }
}
