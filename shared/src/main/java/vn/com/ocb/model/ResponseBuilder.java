package vn.com.ocb.model;

public interface ResponseBuilder {

    <T> BaseResponse<T> buildNoContent();

    <T> BaseResponse<T> buildSuccessResponse(T body);

    <T> BaseResponse<T> buildErrorResponse(Integer errorCode, String error, String message);

    <T> BaseResponse<T> buildErrorResponse(Throwable throwable);

}
