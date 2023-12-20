package vn.com.ocb.usecase.user.handler;

import an.awesome.pipelinr.Command;
import lombok.RequiredArgsConstructor;
import vn.com.ocb.domain.User;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.exception.ResponseCode;
import vn.com.ocb.mapper.UserMapper;
import vn.com.ocb.port.IdGenerator;
import vn.com.ocb.port.PasswordEncoder;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.usecase.user.model.CreateUserRequest;
import vn.com.ocb.usecase.user.model.UserResponse;
import vn.com.ocb.validation.UserValidator;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CreateUserHandler implements Command.Handler<CreateUserRequest, UserResponse> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final IdGenerator idGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse handle(CreateUserRequest request) {
        UserValidator.validateCreateUser(request);
        return CompletableFuture
                .runAsync(() -> {
                    Optional<User> useOpt = userRepository.findByEmail(request.getEmail());
                    if (useOpt.isPresent()) {
                        throw new CoreException(ResponseCode.USER_ERROR_EXISTED);
                    }
                }).thenApplyAsync(res -> {
                    var userToSave = User.builder()
                            .id(idGenerator.generate())
                            .email(request.getEmail())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .lastName(request.getLastName())
                            .firstName(request.getFirstName())
                            .build();

                    return userRepository.save(userToSave);
                }).thenApplyAsync(userMapper::toResponse)
                .join();
    }
}
