package vn.com.ocb.usecase.user.handler;

import lombok.RequiredArgsConstructor;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.domain.ResponseCode;
import vn.com.ocb.domain.User;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.mapper.UserMapper;
import vn.com.ocb.pipeline.request.RequestHandler;
import vn.com.ocb.port.IdGenerator;
import vn.com.ocb.port.PasswordEncoder;
import vn.com.ocb.usecase.user.model.request.CreateUserRequest;
import vn.com.ocb.usecase.user.model.response.UserResponse;

import javax.inject.Inject;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CreateUserHandler extends RequestHandler<CreateUserRequest, UserResponse> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final IdGenerator idGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse handle(CreateUserRequest request) {
        Optional<User> useOpt = userRepository.findByEmail(request.getEmail());
        if (useOpt.isPresent()) {
            throw new CoreException(ResponseCode.USER_ERROR_EXISTED);
        }
        var userToSave = User.builder()
                .id(idGenerator.generate())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .build();

        User saved = userRepository.save(userToSave);
        return userMapper.toResponse(saved);
    }
}
