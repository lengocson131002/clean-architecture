package vn.com.ocb.config;

import java.util.concurrent.CompletableFuture;

public interface RequestNotifier {
    <TRequest extends Request, TResponse> CompletableFuture<TResponse> send(TRequest request);
}
