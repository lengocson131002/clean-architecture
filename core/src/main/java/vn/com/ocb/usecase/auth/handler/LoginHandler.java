package vn.com.ocb.usecase.auth.handler;

import lombok.RequiredArgsConstructor;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.domain.ResponseCode;
import vn.com.ocb.domain.User;
import vn.com.ocb.exception.CoreException;
import vn.com.ocb.mapper.UserMapper;
import vn.com.ocb.pipeline.request.RequestHandler;
import vn.com.ocb.port.PasswordEncoder;
import vn.com.ocb.usecase.auth.model.request.LoginRequest;
import vn.com.ocb.usecase.user.model.response.UserDetailResponse;

import javax.inject.Inject;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LoginHandler extends RequestHandler<LoginRequest, UserDetailResponse> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetailResponse handle(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isEmpty()) {
            throw new CoreException(ResponseCode.AUTH_ERROR_NOT_ALLOWED);
        }

        User user = userOptional.get();
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        if (!hashedPassword.equals(user.getPassword())) {
            throw new CoreException(ResponseCode.AUTH_ERROR_NOT_ALLOWED);
        }

        return userMapper.toDetailResponse(user);
    }
}
