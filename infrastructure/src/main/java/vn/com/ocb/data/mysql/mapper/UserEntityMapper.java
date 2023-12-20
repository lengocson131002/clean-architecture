package vn.com.ocb.data.mysql.mapper;

import vn.com.ocb.data.mysql.entity.UserEntity;
import vn.com.ocb.domain.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserEntityMapper {

    @Inject
    public UserEntityMapper() {
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return UserEntity.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .build();
    }

    public User toDomainModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        return User.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .password(entity.getPassword())
                .build();
    }
}
