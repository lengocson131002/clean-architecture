package vn.com.ocb.data.mysql.repository;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.sqlclient.Pool;
import lombok.extern.slf4j.Slf4j;
import vn.com.ocb.dataprovider.UserRepository;
import vn.com.ocb.domain.User;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
public class UserMysqlRepository implements UserRepository {
    private final Pool sqlPool;
    private static final String SQL_INSERT = "INSERT INTO user (id, email, password, firstName, lastName) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT = "SELECT * FROM user";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM user WHERE id=?";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM user WHERE email=?";
    private static final String SQL_UPDATE = "UPDATE user SET email=?, password=?, firstName=?, lastName=? WHERE id=?";

    private final BaseUserRepository baseUserRepository;

    @Inject
    public UserMysqlRepository(Pool sqlPool) {
        this.sqlPool = sqlPool;
        this.baseUserRepository = new BaseUserRepository(sqlPool);
    }


    @Override
    public User save(User user) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("id", user.getId());
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());

        int rowCounts = baseUserRepository.execute(SQL_INSERT, params);
        if (rowCounts > 0) {
            return user;
        } else {
            log.error("Error when save user");
        }

        return null;
    }

    @Override
    public User update(User user) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("email", user.getEmail());
        params.put("password", user.getPassword());
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("id", user.getId());

        int rowCounts = baseUserRepository.execute(SQL_UPDATE, params);
        if (rowCounts > 0) {
            return user;
        } else {
            log.error("Error when update user");
        }

        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("id", id);
        return baseUserRepository.querySingle(SQL_SELECT_BY_ID, params);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("email", email);
        return baseUserRepository.querySingle(SQL_SELECT_BY_EMAIL, params);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(baseUserRepository.queryMultiple(SQL_SELECT, new LinkedHashMap<>()));
    }
}
