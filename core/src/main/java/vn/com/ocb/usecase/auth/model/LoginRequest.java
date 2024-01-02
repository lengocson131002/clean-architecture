package vn.com.ocb.usecase.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ocb.pipeline.request.Request;
import vn.com.ocb.usecase.user.model.UserDetailResponse;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest extends Request<UserDetailResponse> {
    private String email;
    private String password;

    @Override
    public boolean isValid() {
        return email != null && password != null;
    }
}
