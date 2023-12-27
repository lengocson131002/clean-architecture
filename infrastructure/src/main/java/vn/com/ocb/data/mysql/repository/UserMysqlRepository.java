package vn.com.ocb.data.mysql.repository;

import io.vertx.core.Future;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLClient;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;
import lombok.RequiredArgsConstructor;
import vn.com.ocb.domain.User;
import vn.com.ocb.dataprovider.UserRepository;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserMysqlRepository implements UserRepository {

    private final Pool sqlPool;
    private final Logger logger = LoggerFactory.getLogger(UserMysqlRepository.class);
    private static final String SQL_INSERT = "INSERT INTO user (id, email, password, firstName, lastName) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT = "SELECT * FROM user";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM user WHERE id=?";
    private static final String SQL_SELECT_BY_EMAIL = "SELECT * FROM user WHERE email=?";
    private static final String SQL_UPDATE = "UPDATE user SET email=?, password=?, firstName=?, lastName=? WHERE id=?";

    @Override
    public User save(User user) {
        Tuple params = Tuple.of(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName());

        Future<User> queryFuture = sqlPool.getConnection()
                .compose(conn -> conn.preparedQuery(SQL_INSERT)
                        .execute(params)
                        .map(res -> user)
                        .onComplete(ar -> {
                            conn.close();
                        }));

        CompletableFuture<User> userFuture = queryFuture.toCompletionStage().toCompletableFuture();
        return userFuture.join();
    }

    @Override
    public User update(User user) {
        Tuple params = Tuple.of(
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getId()
        );

        Future<User> queryFuture = sqlPool
                .getConnection()
                .compose(conn -> conn.preparedQuery(SQL_UPDATE)
                        .execute(params)
                        .map(res -> user)
                        .onComplete(ar -> {
                            conn.close();
                        }));

        CompletableFuture<User> userFuture = queryFuture.toCompletionStage().toCompletableFuture();
        return userFuture.join();
    }

    @Override
    public Optional<User> findById(String id) {
        Tuple params = Tuple.of(id);
        return sqlPool.getConnection()
                .compose(conn -> conn
                        .preparedQuery(SQL_SELECT_BY_ID)
                        .execute(params)
                        .map(res -> {
                            for (Row row : res) {
                                User user = User.builder()
                                        .id(row.getString("id"))
                                        .email(row.getString("email"))
                                        .password(row.getString("password"))
                                        .firstName(row.getString("firstName"))
                                        .lastName(row.getString("lastName"))
                                        .build();
                                return Optional.of(user);
                            }
                            return Optional.<User>empty();
                        })
                        .onComplete(ar -> {
                            conn.close();
                        }))
                .toCompletionStage()
                .toCompletableFuture()
                .join();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Tuple params = Tuple.of(email);
        return sqlPool.getConnection()
                .compose(conn -> conn
                        .preparedQuery(SQL_SELECT_BY_EMAIL)
                        .execute(params)
                        .map(rs -> {
                            for (Row row : rs) {
                                User user = User.builder()
                                        .id(row.getString("id"))
                                        .email(row.getString("email"))
                                        .password(row.getString("password"))
                                        .firstName(row.getString("firstName"))
                                        .lastName(row.getString("lastName"))
                                        .build();
                                return Optional.of(user);
                            }
                            return Optional.<User>empty();
                        }).onComplete(ar -> conn.close()))
                .toCompletionStage()
                .toCompletableFuture()
                .join();
    }

    @Override
    public List<User> findAll() {
        CompletableFuture<List<User>> getAllUsers;
        Future<List<User>> usersFuture = sqlPool.getConnection()
                .compose(conn -> conn.query(SQL_SELECT)
                        .execute()
                        .map(rows -> {
                            List<User> users = new ArrayList<>();
                            for (Row row : rows) {
                                User user = User.builder()
                                        .id(row.getString("id"))
                                        .email(row.getString("email"))
                                        .password(row.getString("password"))
                                        .firstName(row.getString("firstName"))
                                        .lastName(row.getString("lastName"))
                                        .build();
                                users.add(user);
                            }
                            return users;
                        }).onComplete(ar -> conn.close()));

        getAllUsers = usersFuture.toCompletionStage().toCompletableFuture();
        return getAllUsers.join();
    }
}
