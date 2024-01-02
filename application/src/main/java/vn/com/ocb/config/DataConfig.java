package vn.com.ocb.config;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import vn.com.ocb.data.inmemory.repository.UserInMemoryRepository;
import vn.com.ocb.data.mysql.constant.MySqlConstants;
import vn.com.ocb.data.mysql.constant.PoolConstants;
import vn.com.ocb.data.mysql.repository.UserMysqlRepository;
import vn.com.ocb.dataprovider.UserRepository;

import javax.inject.Singleton;

@Module(includes = {
        DataConfig.UserInMemoryRepositoryModule.class,
//        DataConfig.UserMqlRepositoryModule.class,
})
public class DataConfig {

    @Module
    interface UserMqlRepositoryModule {
        @Binds
        @Singleton
        UserRepository bindUserRepository(UserMysqlRepository userMysqlRepository);
    }

    @Module
    interface UserInMemoryRepositoryModule {
        @Binds
        @Singleton
        UserRepository bindUserRepository(UserInMemoryRepository userInMemoryRepository);
    }

    @Provides
    @Singleton
    MySQLConnectOptions provideMySQLConnectionOptions() {
        return new MySQLConnectOptions()
                .setPort(MySqlConstants.SQL_PORT)
                .setHost(MySqlConstants.SQL_HOST)
                .setDatabase(MySqlConstants.SQL_DATABASE)
                .setUser(MySqlConstants.SQL_USERNAME)
                .setPassword(MySqlConstants.SQL_PASSWORD);
    }

    // Pool options
    @Provides
    @Singleton
    PoolOptions providePoolOptions() {
        return new PoolOptions()
                .setMaxSize(PoolConstants.POOL_MAX_SIZE);
    }

    @Provides
    @Singleton
    Pool providePool(Vertx vertx, MySQLConnectOptions connectOptions, PoolOptions poolOptions) {
        return MySQLBuilder.pool()
                .with(poolOptions)
                .connectingTo(connectOptions)
                .using(vertx)
                .build();
    }
}
