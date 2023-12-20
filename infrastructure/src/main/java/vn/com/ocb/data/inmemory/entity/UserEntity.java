package vn.com.ocb.data.inmemory.entity;

import lombok.*;

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
