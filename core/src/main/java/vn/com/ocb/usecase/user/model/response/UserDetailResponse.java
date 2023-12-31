package vn.com.ocb.usecase.user.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailResponse {
    private String id;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
}
