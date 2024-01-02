package vn.com.ocb.usecase.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ocb.pipeline.request.Request;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest extends Request<UserResponse> {
    private String userId;
    private String email;
    private String password;
    private String lastName;
    private String firstName;

    @Override
    public boolean isValid() {
        return userId != null
                && email != null
                && password != null
                && lastName != null
                && firstName != null;
    }
}
