package vn.com.ocb;

import io.vertx.core.http.HttpServerOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.com.ocb.config.ApplicationComponent;
import vn.com.ocb.config.DaggerApplicationComponent;

@Slf4j
@SpringBootApplication
public class Main {
    public static void main(String[] args) {

        // Start spring boot application
        SpringApplication.run(Main.class);

        // Start vertx application
        ApplicationComponent serverComponent = DaggerApplicationComponent.builder()
                .build();

        serverComponent.boot().start();
    }

}