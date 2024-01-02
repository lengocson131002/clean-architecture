package vn.com.ocb.pipeline.request;

public abstract class RequestHandler<TRequest extends Request<TResponse>, TResponse> {
    public abstract TResponse handle(TRequest request);
}