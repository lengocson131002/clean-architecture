package vn.com.ocb.exception;

public enum ResponseCode {
    SUCCESS(0, "Success"),
    UNKNOWN_ERROR(1, "Unknown error"),
    COMMON_ERROR(2, "Common error"),
    INTERNAL_SERVER_ERROR(3, "Internal server error"),

    /**
     * User error responses
     */
    USER_ERROR_EXISTED(100, "User existed"),
    USER_ERROR_NOT_FOUND(101, "User not found"),
    USER_ERROR_VALIDATION(102, "User validation error. Field %s is not valid"),

    /**
     * Authentication error responses
     */
    AUTH_ERROR_NOT_ALLOWED(201, "User is not allow to access the system")
    ;

    private final Integer code;
    private final String message;

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
