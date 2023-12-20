package vn.com.rest.config;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Bootstrap {

    private static final Logger log = LoggerFactory.getLogger(Bootstrap.class);

    private final RestServer restServer;

    public void start() {
        restServer.start().onSuccess(res -> {
            log.info("Started server");
        }).onFailure(throwable -> {
            throwable.printStackTrace();
            log.error("Start server failed");
        });
    }

}
