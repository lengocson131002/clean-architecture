package vn.com.ocb.pipeline.request.impl.def;

import lombok.extern.slf4j.Slf4j;
import vn.com.ocb.pipeline.request.Request;
import vn.com.ocb.pipeline.request.RequestHandler;
import vn.com.ocb.pipeline.request.RequestPipeline;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class DefaultRequestPipeline extends RequestPipeline {
    @Override
    public <TRequest extends Request<TResponse>, TResponse> void onHandlerRegistered(
            Class<TRequest> requestClass,
            Class<TResponse> responseClass,
            RequestHandler<TRequest, TResponse> handler) {
        // ignore
        log.info("Registered request handler: {}. Request type: {}. Response type: {}", handler.getClass().getName(), requestClass.getName(), responseClass.getName());
    }

    @Override
    public <TRequest extends Request<TResponse>, TResponse> CompletableFuture<TResponse> handleRequest(TRequest request, RequestHandler<TRequest, TResponse> handler) {
        return CompletableFuture.supplyAsync(() -> handler.handle(request));
    }
}
