package vn.com.ocb;

import io.vertx.core.http.HttpServerOptions;
import vn.com.ocb.config.ApplicationComponent;
import vn.com.ocb.config.DaggerApplicationComponent;

public class Main {
    private static final Integer SERVER_PORT = 8080;

    public static void main(String[] args) {
        HttpServerOptions options = new HttpServerOptions();
        options.setPort(SERVER_PORT);

        ApplicationComponent serverComponent = DaggerApplicationComponent.builder()
                .configuration(options)
                .build();

        serverComponent.boot().start();
    }
}