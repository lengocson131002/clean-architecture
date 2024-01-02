package vn.com.ocb.exception;

import vn.com.ocb.model.response.BaseResponseCode;

public class CoreException extends RuntimeException {
    private final String code;
    private final String error;
    private final String errorMessage;

    public CoreException(BaseResponseCode responseCode) {
        this.code = responseCode.getCode();
        this.error = responseCode.toString();
        this.errorMessage = responseCode.getMessage();
    }

    public CoreException(BaseResponseCode responseCode, Object... params) {
        this.code = responseCode.getCode();
        this.error = responseCode.toString();
        this.errorMessage = String.format(responseCode.getMessage(), params);
    }

    public CoreException(String errorCode, String message) {
        this.code = errorCode;
        this.errorMessage = message;
        this.error = "";
    }

    public CoreException(String errorCode, String error, String message) {
        this.code = errorCode;
        this.errorMessage = message;
        this.error = error;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.errorMessage;
    }

    public String getError() {
        return this.error;
    }
}
