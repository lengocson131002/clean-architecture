package vn.com.ocb.exception;

public class CoreException extends RuntimeException {
    private final Integer code;
    private final String error;
    private final String errorMessage;

    public CoreException(ResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.error = responseCode.toString();
        this.errorMessage = responseCode.getMessage();
    }

    public CoreException(ResponseCode responseCode, Object... params) {
        this.code = responseCode.getCode();
        this.error = responseCode.toString();
        this.errorMessage = String.format(responseCode.getMessage(), params);
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.errorMessage;
    }

    public String getError() {
        return this.error;
    }
}
