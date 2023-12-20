package vn.com.ocb.usecase.auth.model;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ocb.usecase.user.model.UserDetailResponse;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest implements Command<UserDetailResponse> {
    private String email;
    private String password;
}
