package vn.com.ocb.config;

import java.util.concurrent.CompletableFuture;

public interface RequestHandler<TRequest extends Request, TResponse> {
    CompletableFuture<TResponse> handle(final TRequest request);
}

