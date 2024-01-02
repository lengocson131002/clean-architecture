package vn.com.ocb.pipeline.request;

public class RequestHandlerExistedException extends RuntimeException {
    private final String message;

    public RequestHandlerExistedException(RequestHandler<?, ?> handler) {
        String handlerName = handler.getClass().getName();
        message = String.format("%s existed.", handlerName);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
