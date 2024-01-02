package vn.com.ocb.usecase.user.handler;

import lombok.RequiredArgsConstructor;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.domain.User;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.mapper.UserMapper;
import vn.com.ocb.pipeline.request.RequestHandler;
import vn.com.ocb.port.PasswordEncoder;
import vn.com.ocb.usecase.user.model.UpdateUserRequest;
import vn.com.ocb.usecase.user.model.UserResponse;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UpdateUserHandler extends RequestHandler<UpdateUserRequest, UserResponse> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse handle(UpdateUserRequest request) {
        return CompletableFuture.supplyAsync(() -> {
                    Optional<User> userOptional = userRepository.findById(request.getUserId());
                    if (userOptional.isEmpty()) {
                        throw new CoreException(ResponseCode.USER_ERROR_NOT_FOUND);
                    }
                    return userOptional.get();
                }).thenApplyAsync(user -> {
                    if (!user.getEmail().equals(request.getEmail())) {
                        Optional<User> existedUser = userRepository
                                .findByEmail(request.getEmail());

                        if (existedUser.isPresent()) {
                            throw new CoreException(ResponseCode.USER_ERROR_EXISTED);
                        }
                    }
                    return user;
                }).thenApplyAsync(user -> {
                    user.setEmail(request.getEmail())
                            .setPassword(passwordEncoder.encode(request.getPassword()))
                            .setFirstName(request.getFirstName())
                            .setLastName(request.getLastName());

                    return userRepository.update(user);
                }).thenApplyAsync(userMapper::toResponse)
                .join();

    }
}
