package vn.com.rest.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.model.BaseResponse;
import vn.com.rest.utils.ResponseHelper;

import javax.inject.Inject;

public class FailureHandler implements Handler<RoutingContext> {


    @Inject
    public FailureHandler() {

    }

    @Override
    public void handle(RoutingContext rc) {
        Throwable exception = rc.failure().getCause();
        if (exception == null) {
            return;
        }

        if (exception instanceof CoreException) {
            CoreException coreException = ((CoreException) exception);
            BaseResponse<Object> errorResponse = BaseResponse
                    .builder()
                    .code(coreException.getCode())
                    .message(coreException.getMessage())
                    .error(coreException.getError())
                    .build();
            ResponseHelper.sendJson(rc, errorResponse, 400);
        } else {
            // handle internal server error
            BaseResponse<Object> errorResponse = BaseResponse
                    .builder()
                    .code(ResponseCode.INTERNAL_SERVER_ERROR.getCode())
                    .message(ResponseCode.INTERNAL_SERVER_ERROR.getMessage())
                    .error(ResponseCode.INTERNAL_SERVER_ERROR.toString())
                    .build();
            ResponseHelper.sendJson(rc, errorResponse, 500);
        }
    }
}
