package vn.com.ocb.usecase.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ocb.pipeline.request.Request;
import vn.com.ocb.usecase.user.model.response.UserDetailResponse;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetUserRequest extends Request<UserDetailResponse> {
    private String userId;

    @Override
    public boolean isValid() {
        return userId != null;
    }
}
