package vn.com.ocb.pipeline.request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class RequestPipeline {

    private final ConcurrentMap<Class<? extends Request<?>>, RequestHandler<?, ?>> handlers = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public synchronized <TRequest extends Request<TResponse>, TResponse> RequestPipeline register(RequestHandler<TRequest, TResponse> handler) {
        // Extract request type and response type
        Type superClass = handler.getClass().getGenericSuperclass();
        if (superClass instanceof Class) {
            throw new IllegalArgumentException("Internal error: RequestHandler constructed without actual type information");
        }

        Type[] parameterTypes = ((ParameterizedType) superClass).getActualTypeArguments();
        Type requestType = parameterTypes[0];
        if (!(requestType instanceof Class)) {
            throw new IllegalArgumentException(String.format("Internal error: Request must be class type. Provided: %s", requestType.getTypeName()));
        }

        Type responseType = parameterTypes[1];
        if (!(responseType instanceof Class)) {
            throw new IllegalArgumentException(String.format("Internal error: Response must be class type. Provided: %s", responseType.getTypeName()));
        }

        if (handlers.containsKey(requestType)) {
            throw new RequestHandlerExistedException(handler);
        }
        Class<TRequest> requestClass = (Class<TRequest>) requestType;
        Class<TResponse> responseClass = (Class<TResponse>) responseType;

        handlers.put(requestClass, handler);
        onHandlerRegistered(requestClass, responseClass, handler);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <TResponse, TRequest extends Request<TResponse>> CompletableFuture<TResponse> send(TRequest request) {
        if (!handlers.containsKey(request.getClass())) {
            throw new RequestHandlerNotFoundException(request);
        }
        if (!request.isValid()) {
            throw new InvalidRequestException(request);
        }
        RequestHandler<TRequest, TResponse> handler = getHandler(request.getClass());
        if (handler == null) {
            throw new RequestHandlerNotFoundException(request.getClass());
        }

        return handleRequest(request, handler);
    }

    @SuppressWarnings("unchecked")
    public <TResponse, TRequest extends Request<TResponse>> RequestHandler<TRequest, TResponse> getHandler(Class<TRequest> requestType) {
        if (!handlers.containsKey(requestType)) {
            throw new RequestHandlerNotFoundException(requestType);
        }
        return (RequestHandler<TRequest, TResponse>) handlers.get(requestType);
    }

    public abstract <TRequest extends Request<TResponse>, TResponse> void onHandlerRegistered(
            Class<TRequest> requestType,
            Class<TResponse> responseType,
            RequestHandler<TRequest, TResponse> handler);


    public abstract <TRequest extends Request<TResponse>, TResponse> CompletableFuture<TResponse> handleRequest(
            TRequest request, RequestHandler<TRequest, TResponse> handler);

}
