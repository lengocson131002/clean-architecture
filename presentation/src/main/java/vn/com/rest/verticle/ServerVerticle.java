package vn.com.rest.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.LoggerHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.com.rest.handler.FailureHandler;
import vn.com.rest.handler.UserHandler;

import javax.inject.Inject;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ServerVerticle extends AbstractVerticle {

    private final HttpServer httpServer;
    private final UserHandler userHandler;
    private final FailureHandler failureHandler;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);
        setupRouter(router);
        httpServer
                .requestHandler(router)
                .listen()
                .onSuccess(res -> {
                    log.info("✅ Server started successfully...");
                    startPromise.complete();
                }).onFailure(throwable -> {
                    log.info("✅ Server started failed..");
                    startPromise.fail(throwable);
                });
    }

    private void setupRouter(Router router) {
        router.route().handler(BodyHandler.create());
        router.route().handler(LoggerHandler.create());
        router.route().failureHandler(failureHandler);
        addUserRoutes(router);
    }

    private void addUserRoutes(Router router) {
        router.post("/api/v1/users").handler(userHandler::handleCreateUser);
        router.put("/api/v1/users/:userId").handler(userHandler::handleUpdateUser);
        router.get("/api/v1/login").handler(userHandler::handleLoginUser);
        router.get("/api/v1/users/:userId").handler(userHandler::handleGetUserById);
        router.get("/api/v1/users").handler(userHandler::handleGetAllUsers);
    }


    @Override
    public void stop() throws Exception {
        if (httpServer != null) {
            httpServer.close();
        }
    }
}
