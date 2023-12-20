package vn.com.ocb.usecase.user.model;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest implements Command<UserResponse> {
    private String email;
    private String password;
    private String lastName;
    private String firstName;
}
