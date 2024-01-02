package vn.com.ocb.mapper;

import vn.com.ocb.domain.User;
import vn.com.ocb.usecase.user.model.UserDetailResponse;
import vn.com.ocb.usecase.user.model.UserResponse;

import javax.inject.Inject;
import java.time.LocalDateTime;

public class UserMapper {

    @Inject
    public UserMapper() {

    }

    public UserResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public UserDetailResponse toDetailResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserDetailResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


}
