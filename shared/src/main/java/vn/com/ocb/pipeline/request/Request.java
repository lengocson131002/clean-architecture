package vn.com.ocb.pipeline.request;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class Request<TResponse> {

    private String requestId = UUID.randomUUID().toString();

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public abstract boolean isValid();

    public CompletableFuture<TResponse> execute(RequestPipeline pipeline) {
        return pipeline.send(this);
    }

}