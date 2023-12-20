package vn.com.ocb.data.inmemory.repository;

import lombok.RequiredArgsConstructor;
import vn.com.ocb.data.mysql.entity.UserEntity;
import vn.com.ocb.data.mysql.mapper.UserEntityMapper;
import vn.com.ocb.domain.User;
import vn.com.ocb.dataprovider.UserRepository;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserInMemoryRepository implements UserRepository {
    private final Map<String, UserEntity> inMemoryDb = new Hashtable<>();
    private final UserEntityMapper userEntityMapper;

    @Override
    public User save(final User user) {
        inMemoryDb.put(user.getId(), userEntityMapper.toEntity(user));
        return user;
    }

    @Override
    public User update(User user) {
        inMemoryDb.put(user.getId(), userEntityMapper.toEntity(user));
        return user;
    }

    @Override
    public Optional<User> findById(final String id) {
        UserEntity entity = inMemoryDb.get(id);
        return Optional.ofNullable(userEntityMapper.toDomainModel(entity));
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        Optional<UserEntity> entityOptional = inMemoryDb.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny();
        return entityOptional.isEmpty()
                ? Optional.empty()
                : Optional.of(userEntityMapper.toDomainModel(entityOptional.get()));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(inMemoryDb.values()
                .stream()
                .map(userEntityMapper::toDomainModel)
                .collect(Collectors.toList()));
    }
}
