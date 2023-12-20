package vn.com.ocb.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * Business domain models
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {
    private String id;
    private String email;
    private String password;
    private String lastName;
    private String firstName;
}
