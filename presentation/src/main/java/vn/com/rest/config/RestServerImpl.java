package vn.com.rest.config;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lombok.RequiredArgsConstructor;
import vn.com.rest.verticle.ServerVerticle;

import javax.inject.Inject;
import javax.inject.Provider;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RestServerImpl implements RestServer {

    private final Vertx vertx;
    private final Provider<ServerVerticle> serverVerticle;

    @Override
    public Future<Void> start() {
        return Future.future(promise -> {
            vertx.deployVerticle(serverVerticle.get())
                    .onSuccess(res -> promise.complete())
                    .onFailure(promise::fail);
        });
    }

    @Override
    public Future<Void> stop() {
        return Future.future(promise -> {
            vertx.close()
                    .onSuccess(promise::complete)
                    .onFailure(promise::fail);
        });
    }
}
