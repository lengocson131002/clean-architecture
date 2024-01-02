package vn.com.ocb.pipeline.request;

import java.util.concurrent.CompletableFuture;

public abstract class Request<TResponse> {

    public abstract boolean isValid();

    public CompletableFuture<TResponse> execute(RequestPipeline pipeline) {
        return pipeline.send(this);
    }

}