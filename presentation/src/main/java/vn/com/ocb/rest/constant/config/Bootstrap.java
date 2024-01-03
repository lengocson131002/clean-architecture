package vn.com.ocb.rest.constant.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class Bootstrap {

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
