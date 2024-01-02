package vn.com.ocb.usecase.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@Builder
public class GetAllUserResponse extends ArrayList<UserResponse> {
}
