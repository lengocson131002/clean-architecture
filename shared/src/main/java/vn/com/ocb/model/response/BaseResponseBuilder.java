package vn.com.ocb.model.response;

import vn.com.ocb.exception.CoreException;

public class BaseResponseBuilder {

    public static int HTTP_STATUS_CODE_SUCCESS = 200;

    public static int HTTP_STATUS_CODE_BAD_REQUEST = 200;

    public static int HTTP_STATUS_CODE_INTERNAL_SERVER_ERROR = 500;


    public static <T> BaseResponse<T> buildSuccessResponse(String requestId) {
        return buildSuccessResponse(requestId, null);
    }

    public static <T> BaseResponse<T> buildSuccessResponse(String requestId, T body) {
        return BaseResponse.<T>builder()
                .requestId(requestId)
                .statusCode(HTTP_STATUS_CODE_SUCCESS)
                .data(body)
                .build();
    }

    public static <T> BaseResponse<T> buildErrorResponse(String requestId, String code, String error, String message) {
        return BaseResponse.<T>builder()
                .requestId(requestId)
                .statusCode(HTTP_STATUS_CODE_BAD_REQUEST)
                .code(code)
                .error(error)
                .message(message)
                .build();
    }

    public static <T> BaseResponse<T> buildErrorResponse(String requestId, Throwable throwable) {
        if (throwable instanceof CoreException) {
            CoreException exception = (CoreException) throwable;
            return BaseResponse.<T>builder()
                    .statusCode(HTTP_STATUS_CODE_BAD_REQUEST)
                    .requestId(requestId)
                    .code(exception.getCode())
                    .error(exception.getError())
                    .message(exception.getMessage())
                    .build();
        }
        return BaseResponse.<T>builder()
                .requestId(requestId)
                .statusCode(HTTP_STATUS_CODE_INTERNAL_SERVER_ERROR)
                .code("INTERNAL_SERVER_ERROR")
                .error("Internal server error")
                .message(throwable.getMessage())
                .build();
    }

}
