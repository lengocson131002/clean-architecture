package vn.com.ocb.usecase.user.model;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest implements Command<UserResponse> {
    private String userId;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
}
