package vn.com.ocb.data.mysql.repository;

import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.sqlclient.Pool;
import lombok.RequiredArgsConstructor;
import vn.com.ocb.domain.User;
import vn.com.ocb.dataprovider.UserRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserMysqlRepository implements UserRepository {

    private final Pool sqlPool;
    private final Logger logger = LoggerFactory.getLogger(UserMysqlRepository.class);

    private static final String SQL_INSERT = "INSERT INTO user (id, email, password, firstName, lastName) VALUES (?, ?, ?, ?, ?)";

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public Optional<User> findById(String id) {
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

//    @Override
//    public User save(User user) {
//        Tuple params = Tuple.of(
//                user.getId(),
//                user.getEmail(),
//                user.getPassword(),
//                user.getFirstName(),
//                user.getLastName());
//
//        CompletableFuture<User> insertUser = new CompletableFuture<>();
//        sqlPool.getConnection()
//                .compose(conn -> conn.preparedQuery(SQL_INSERT)
//                        .execute(params)
//                        .onComplete(ar -> {
//                            if (ar.succeeded()) {
//                                insertUser.complete(user);
//                            } else {
//                                insertUser.completeExceptionally(ar.cause());
//                            }
//                            conn.close();
//                        }))
//                .onComplete(ar -> {
//                    if (ar.succeeded()) {
//                        logger.info("Execute query successfully");
//                    } else {
//                        logger.error(String.format("Some thing went wrong %s", ar.cause().getMessage()));
//                    }
//                });
//
//        return insertUser.join();
//    }
//
//    @Override
//    public User update(User user) {
//        return null;
//    }
//
//    @Override
//    public Optional<User> findById(String id) {
//        return Optional.empty();
//    }
//
//    @Override
//    public Optional<User> findByEmail(String email) {
//        return Optional.empty();
//    }
//
//    @Override
//    public List<User> findAll() {
//        CompletableFuture<List<User>> getAllUsers = new CompletableFuture<>();
//        sqlPool.getConnection()
//                .compose(conn -> conn
//                        .query(SQL_INSERT)
//                        .execute()
//                        .onComplete(ar -> {
//                                    conn.close();
//                                    if (!ar.succeeded()) {
//                                        getAllUsers.completeExceptionally(ar.cause());
//                                        return;
//                                    }
//
//                                    List<User> users = new ArrayList<>();
//                                    for (Row row : ar.result()) {
//                                        User user = User.builder()
//                                                .id(row.getString("id"))
//                                                .email(row.getString("email"))
//                                                .password(row.getString("password"))
//                                                .firstName(row.getString("firstName"))
//                                                .lastName(row.getString("lastName"))
//                                                .build();
//                                        users.add(user);
//                                    }
//
//                                    getAllUsers.complete(users);
//                                }
//                        ))
//                .onComplete(ar -> {
//                    if (ar.succeeded()) {
//                        logger.info("Execute query successfully");
//                    } else {
//                        logger.error(String.format("Some thing went wrong %s", ar.cause().getMessage()));
//                    }
//                });
//
//
//        return getAllUsers.join();
//    }
}
