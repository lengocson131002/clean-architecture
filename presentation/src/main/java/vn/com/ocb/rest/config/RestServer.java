package vn.com.ocb.rest.config;

import io.vertx.core.Future;

public interface RestServer {

    Future<Void> start();

    Future<Void> stop();

}
