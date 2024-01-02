package vn.com.ocb.pipeline.request;

public class RequestHandlerNotFoundException extends RuntimeException {

    private final String requestClass;

    public RequestHandlerNotFoundException(Class<?> requestType) {
        this.requestClass = requestType.getSimpleName();
    }

    public RequestHandlerNotFoundException(Request<?> command) {
        this.requestClass = command.getClass().getSimpleName();
    }

    @Override
    public String getMessage() {
        return "Cannot find a matching handler for " + requestClass + " request";
    }
}
