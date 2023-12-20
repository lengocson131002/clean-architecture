package vn.com.ocb.validation;

import org.apache.commons.lang3.StringUtils;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.usecase.user.model.CreateUserRequest;

public class UserValidator {

    public static void validateCreateUser(final CreateUserRequest request) {
        if (request == null) {
            throw new CoreException(ResponseCode.USER_ERROR_VALIDATION, "request");
        }
        if (StringUtils.isBlank(request.getEmail())) {
            throw new CoreException(ResponseCode.USER_ERROR_VALIDATION, "email");
        }
        if (StringUtils.isBlank(request.getFirstName())) {
            throw new CoreException(ResponseCode.USER_ERROR_VALIDATION, "firstName");
        }
        if (StringUtils.isBlank(request.getLastName())) {
            throw new CoreException(ResponseCode.USER_ERROR_VALIDATION, "lastName");
        }
    }

    private UserValidator() {

    }
}
