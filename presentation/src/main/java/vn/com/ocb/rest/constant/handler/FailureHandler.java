package vn.com.ocb.rest.constant.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;

@Slf4j
public class FailureHandler implements Handler<RoutingContext> {
    @Inject
    public FailureHandler() {

    }

    @Override
    public void handle(RoutingContext rc) {
        Throwable exception = rc.failure();
        if (exception == null) {
            return;
        }

        log.error("Exception: {}", exception.getMessage());
    }
}
