package vn.com.ocb.usecase.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
}
