package vn.com.ocb.usecase.user.handler;

import lombok.RequiredArgsConstructor;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.domain.ResponseCode;
import vn.com.ocb.domain.User;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.mapper.UserMapper;
import vn.com.ocb.pipeline.request.RequestHandler;
import vn.com.ocb.port.PasswordEncoder;
import vn.com.ocb.usecase.user.model.request.UpdateUserRequest;
import vn.com.ocb.usecase.user.model.response.UserResponse;

import javax.inject.Inject;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UpdateUserHandler extends RequestHandler<UpdateUserRequest, UserResponse> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse handle(UpdateUserRequest request) {
        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty()) {
            throw new CoreException(ResponseCode.USER_ERROR_NOT_FOUND);
        }

        User user = userOptional.get();
        if (!user.getEmail().equals(request.getEmail())) {
            Optional<User> existedUser = userRepository
                    .findByEmail(request.getEmail());

            if (existedUser.isPresent()) {
                throw new CoreException(ResponseCode.USER_ERROR_EXISTED);
            }
        }

        user.setEmail(request.getEmail())
                .setPassword(passwordEncoder.encode(request.getPassword()))
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName());

        User saved = userRepository.update(user);
        return userMapper.toResponse(saved);
    }
}
