package vn.com.rest.utils;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.model.BaseResponse;

public class ResponseHelper {

    private ResponseHelper() {
    }

    public static Integer DEFAULT_HTTP_STATUS_CODE = 200;

    public static <T> void sendJson(RoutingContext rc, BaseResponse<T> response, int httpStatusCode) {
        HttpServerResponse httpServerResponse = rc.response();
        httpServerResponse.setStatusCode(httpStatusCode);
        httpServerResponse.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        JsonObject jsonObject = JsonObject.mapFrom(response);
        rc.end(jsonObject.encodePrettily());
    }

    public static <T> void sendJson(RoutingContext rc, T data) {
        BaseResponse<Object> response = BaseResponse
                .builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(ResponseCode.SUCCESS.getMessage())
                .data(data)
                .build();
        sendJson(rc, response);
    }

    public static <T> void sendJson(RoutingContext rc, BaseResponse<T> response) {
        sendJson(rc, response, DEFAULT_HTTP_STATUS_CODE);
    }
}
