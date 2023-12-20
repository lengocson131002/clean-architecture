package vn.com.ocb.data.mysql.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    private String id;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
}
