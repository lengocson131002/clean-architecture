package vn.com.ocb.usecase.auth.handler;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;
import vn.com.ocb.domain.User;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.mapper.UserMapper;
import vn.com.ocb.port.PasswordEncoder;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.usecase.auth.model.LoginRequest;
import vn.com.ocb.usecase.user.model.UserDetailResponse;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LoginHandler implements Command.Handler<LoginRequest, UserDetailResponse> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailResponse handle(LoginRequest request) {
        return CompletableFuture.supplyAsync(() -> {
                    Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
                    if (userOptional.isEmpty()) {
                        throw new CoreException(ResponseCode.AUTH_ERROR_NOT_ALLOWED);
                    }

                    return userOptional.get();
                }).thenApplyAsync(user -> {
                    String hashedPassword = passwordEncoder.encode(request.getPassword());
                    if (!hashedPassword.equals(user.getPassword())) {
                        throw new CoreException(ResponseCode.AUTH_ERROR_NOT_ALLOWED);
                    }
                    return user;
                }).thenApplyAsync(userMapper::toDetailResponse)
                .join();
    }
}
