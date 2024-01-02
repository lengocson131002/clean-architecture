package vn.com.ocb.usecase.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ocb.pipeline.request.Request;

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
