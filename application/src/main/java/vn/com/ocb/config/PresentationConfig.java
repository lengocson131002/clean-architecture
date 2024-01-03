package vn.com.ocb.config;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import vn.com.ocb.rest.config.RestServer;
import vn.com.ocb.rest.config.RestServerImpl;

import javax.inject.Singleton;

@Module(includes = {
        PresentationConfig.RestServerModule.class
})
public class PresentationConfig {

    @Module
    interface RestServerModule {
        @Binds
        RestServer bindRestServer(RestServerImpl restServer);
    }

    @Provides
    @Singleton
    public VertxOptions vertxOptions() {
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setPreferNativeTransport(true);
        return vertxOptions;
    }

    @Provides
    @Singleton
    public Vertx vertx(VertxOptions options) {
        return Vertx.vertx(options);
    }

    @Provides
    @Singleton
    public EventBus eventBus(Vertx vertx) {
        return vertx.eventBus();
    }

    @Provides
    @Singleton
    public HttpServer httpServer(Vertx vertx, HttpServerOptions options) {
        return vertx.createHttpServer(options);
    }

    @Provides
    public Router router(Vertx vertx) {
        return Router.router(vertx);
    }

    @Provides
    @Singleton
    public HttpServerOptions httpServerOptions() {
        // Vertx application
        HttpServerOptions options = new HttpServerOptions();
        options.setPort(8080);
        return options;
    }
}
