package vn.com.ocb.usecase.user.handler;

import lombok.RequiredArgsConstructor;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.domain.User;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.mapper.UserMapper;
import vn.com.ocb.pipeline.request.RequestHandler;
import vn.com.ocb.usecase.user.model.GetUserRequest;
import vn.com.ocb.usecase.user.model.UserDetailResponse;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class GetUserHandler extends RequestHandler<GetUserRequest, UserDetailResponse> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetailResponse handle(GetUserRequest request) {
        return CompletableFuture.supplyAsync(() -> {
                    Optional<User> userOptional = userRepository.findById(request.getUserId());
                    if (userOptional.isEmpty()) {
                        throw new CoreException(ResponseCode.USER_ERROR_NOT_FOUND);
                    }
                    return userOptional.get();
                }).thenApplyAsync(userMapper::toDetailResponse)
                .join();
    }
}
