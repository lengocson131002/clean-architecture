package vn.com.ocb.usecase.user.model;

import an.awesome.pipelinr.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetUserRequest implements Command<UserDetailResponse> {
    private String userId;
}
