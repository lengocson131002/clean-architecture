package vn.com.ocb.config;

import an.awesome.pipelinr.Pipeline;
import an.awesome.pipelinr.Pipelinr;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import vn.com.ocb.adapter.IdGeneratorImpl;
import vn.com.ocb.adapter.PasswordEncoderImpl;
import vn.com.ocb.port.IdGenerator;
import vn.com.ocb.port.PasswordEncoder;
import vn.com.ocb.usecase.auth.handler.LoginHandler;
import vn.com.ocb.usecase.user.handler.CreateUserHandler;
import vn.com.ocb.usecase.user.handler.GetAllUsersHandler;
import vn.com.ocb.usecase.user.handler.GetUserHandler;
import vn.com.ocb.usecase.user.handler.UpdateUserHandler;

import javax.inject.Singleton;
import java.util.stream.Stream;

@Module
public final class CoreConfig {

    @Provides
    @Singleton
    public Pipeline provideUseCasePipeline(
            GetAllUsersHandler getAllUsersHandler,
            GetUserHandler getUserHandler,
            UpdateUserHandler updateUserHandler,
            CreateUserHandler createUserHandler,
            LoginHandler loginHandler) {
        return new Pipelinr().with(() -> Stream
                .of(getAllUsersHandler,
                        getUserHandler,
                        createUserHandler,
                        updateUserHandler,
                        loginHandler));
    }
}
