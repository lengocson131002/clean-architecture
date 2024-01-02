package vn.com.ocb.usecase.user.handler;

import lombok.RequiredArgsConstructor;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.mapper.UserMapper;
import vn.com.ocb.pipeline.request.RequestHandler;
import vn.com.ocb.usecase.user.model.request.GetAllUserRequest;
import vn.com.ocb.usecase.user.model.request.GetAllUserResponse;
import vn.com.ocb.usecase.user.model.response.UserResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GetAllUsersHandler extends RequestHandler<GetAllUserRequest, GetAllUserResponse> {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public GetAllUserResponse handle(GetAllUserRequest request) {
        List<UserResponse> users = userRepository
                .findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());

        GetAllUserResponse response = new GetAllUserResponse();
        response.addAll(users);
        return response;
    }

}