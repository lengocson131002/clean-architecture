package vn.com.ocb.data.mysql.repository;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlResult;
import io.vertx.sqlclient.Tuple;
import org.apache.commons.lang3.NotImplementedException;
import vn.com.ocb.domain.User;
import vn.com.ocb.internal.persistence.BaseSqlExecute;
import vn.com.ocb.internal.persistence.BaseSqlQuery;

import java.util.*;

public class BaseUserRepository implements BaseSqlExecute, BaseSqlQuery<User> {
    private final Pool sqlPool;

    public BaseUserRepository(Pool pool) {
        this.sqlPool = pool;
    }

    @Override
    public int execute(String sql, LinkedHashMap<String, Object> params) {
        Tuple tupleParams = Tuple.from(params.values().toArray());
        Future<Integer> queryFuture = sqlPool.getConnection()
                .compose(conn -> conn.preparedQuery(sql)
                        .execute(tupleParams)
                        .map(SqlResult::rowCount)
                        .onComplete(ar -> {
                            conn.close();
                        }));

        return queryFuture
                .toCompletionStage()
                .toCompletableFuture().join();
    }

    @Override
    public int executeWithTransaction(String sql, LinkedHashMap<String, Object> params) {
        throw new NotImplementedException();
    }

    @Override
    public Optional<User> querySingle(String sql, LinkedHashMap<String, Object> params) {
        Tuple tupleParams = Tuple.from(params.values().toArray());
        Future<Optional<User>> queryFuture = sqlPool.getConnection()
                .compose(conn -> conn
                        .preparedQuery(sql)
                        .execute(tupleParams)
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
                        }));
        return queryFuture.toCompletionStage()
                .toCompletableFuture()
                .join();
    }

    @Override
    public Collection<User> queryMultiple(String sql, LinkedHashMap<String, Object> params) {

        Tuple tupleParams = Tuple.from(params.values().toArray());
        Future<List<User>> queryFuture = sqlPool.getConnection()
                .compose(conn -> conn
                        .preparedQuery(sql)
                        .execute(tupleParams)
                        .map(res -> {
                            List<User> users = new ArrayList<>();
                            for (Row row : res) {
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
                        })
                        .onComplete(ar -> {
                            conn.close();
                        }));
        return queryFuture.toCompletionStage()
                .toCompletableFuture()
                .join();
    }
}
