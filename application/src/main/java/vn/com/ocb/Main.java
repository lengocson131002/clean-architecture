package vn.com.ocb;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.sqlclient.Pool;
import lombok.extern.slf4j.Slf4j;
import vn.com.ocb.config.ApplicationComponent;
import vn.com.ocb.config.DaggerApplicationComponent;

@Slf4j
public class Main {
    private static final Integer SERVER_PORT = 8080;

    public static void main(String[] args) {
        HttpServerOptions options = new HttpServerOptions();
        options.setPort(SERVER_PORT);

        ApplicationComponent serverComponent = DaggerApplicationComponent.builder()
                .configuration(options)
                .build();

        serverComponent.boot().start();

        // Config for timestamp
        DatabindCodec.mapper().findAndRegisterModules();
    }

    private static void logPool(Pool pool) {
        while (true) {
            try {
                Thread.sleep(1000);
                log.info("Available connection: " + pool.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}