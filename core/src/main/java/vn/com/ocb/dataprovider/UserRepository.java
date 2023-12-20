package vn.com.ocb.dataprovider;

import vn.com.ocb.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    User update(User user);

    Optional<User> findById(String id);

    Optional<User> findByEmail(String email);

    List<User> findAll();
}
