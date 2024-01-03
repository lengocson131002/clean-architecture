package vn.com.ocb.usecase.user.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ocb.pipeline.request.Request;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllUserRequest extends Request<GetAllUserResponse> {

    private String search;

    @Override
    public boolean isValid() {
        return true;
    }
}
