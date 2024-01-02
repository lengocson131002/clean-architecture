package vn.com.ocb.usecase.user.model.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.ocb.usecase.user.model.response.UserResponse;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@Builder
public class GetAllUserResponse extends ArrayList<UserResponse> {
}
