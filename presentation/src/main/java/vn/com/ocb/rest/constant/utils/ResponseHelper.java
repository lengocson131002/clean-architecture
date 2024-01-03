package vn.com.ocb.rest.constant.utils;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import vn.com.ocb.model.response.BaseResponse;
import vn.com.ocb.model.response.BaseResponseBuilder;

public class ResponseHelper {

    private ResponseHelper() {
    }

    public static <T> void sendJson(RoutingContext rc, BaseResponse<T> response) {
        HttpServerResponse httpServerResponse = rc.response();
        httpServerResponse.setStatusCode(response.getStatusCode());
        httpServerResponse.putHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        JsonObject jsonObject = JsonObject.mapFrom(response);
        rc.end(jsonObject.encodePrettily());
    }

    public static <T> void sendJson(RoutingContext rc, String requestId, T data) {
        sendJson(rc, BaseResponseBuilder.buildSuccessResponse(requestId, data));
    }


    public static void sendError(RoutingContext rc, String requestId, Throwable throwable) {
        sendJson(rc, BaseResponseBuilder.buildErrorResponse(requestId, throwable));
    }
}
