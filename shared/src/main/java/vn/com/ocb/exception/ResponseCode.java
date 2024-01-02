package vn.com.ocb.exception;

import vn.com.ocb.model.BaseResponseCode;

public enum ResponseCode implements BaseResponseCode {
    SUCCESS("0", "Success"),
    UNKNOWN_ERROR("1", "Unknown error"),
    COMMON_ERROR("2", "Common error"),
    INTERNAL_SERVER_ERROR("3", "Internal server error"),


    USER_ERROR_EXISTED("100", "User existed"),
    USER_ERROR_NOT_FOUND("101", "User not found"),
    USER_ERROR_VALIDATION("102", "User validation error. Field %s is not valid"),

    AUTH_ERROR_NOT_ALLOWED("201", "User is not allow to access the system");

    private final String code;
    private final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
