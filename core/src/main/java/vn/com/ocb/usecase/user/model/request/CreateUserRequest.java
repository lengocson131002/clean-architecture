package vn.com.ocb.usecase.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.domain.ResponseCode;
import vn.com.ocb.pipeline.request.Request;
import vn.com.ocb.usecase.user.model.response.UserResponse;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest extends Request<UserResponse> {
    private String email;
    private String password;
    private String lastName;
    private String firstName;

    @Override
    public boolean isValid() {
        if (StringUtils.isBlank(this.getEmail())) {
            throw new CoreException(ResponseCode.USER_ERROR_VALIDATION, "email");
        }
        if (StringUtils.isBlank(this.getFirstName())) {
            throw new CoreException(ResponseCode.USER_ERROR_VALIDATION, "firstName");
        }
        if (StringUtils.isBlank(this.getLastName())) {
            throw new CoreException(ResponseCode.USER_ERROR_VALIDATION, "lastName");
        }

        return true;
    }
}
