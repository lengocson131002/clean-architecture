package vn.com.ocb.usecase.user.handler;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;
import vn.com.ocb.mapper.UserMapper;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.usecase.user.model.GetAllUserRequest;
import vn.com.ocb.usecase.user.model.UserResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GetAllUsersHandler implements Command.Handler<GetAllUserRequest, List<UserResponse>> {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public List<UserResponse> handle(GetAllUserRequest request) {
        return CompletableFuture
                .supplyAsync(userRepository::findAll)
                .thenApplyAsync(users -> users
                        .stream()
                        .map(userMapper::toResponse)
                        .collect(Collectors.toList()))
                .join();
    }
}
