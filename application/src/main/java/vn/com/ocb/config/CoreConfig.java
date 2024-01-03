package vn.com.ocb.config;

import dagger.Module;
import dagger.Provides;
import vn.com.ocb.pipeline.request.RequestPipeline;
import vn.com.ocb.pipeline.request.impl.def.DefaultRequestPipeline;
import vn.com.ocb.pipeline.request.impl.kafka.KafkaRequestPipeline;
import vn.com.ocb.usecase.auth.handler.LoginHandler;
import vn.com.ocb.usecase.user.handler.CreateUserHandler;
import vn.com.ocb.usecase.user.handler.GetAllUsersHandler;
import vn.com.ocb.usecase.user.handler.GetUserHandler;
import vn.com.ocb.usecase.user.handler.UpdateUserHandler;

import javax.inject.Singleton;

@Module
public final class CoreConfig {
    @Provides
    @Singleton
    public RequestPipeline provideUseCasePipeline(
            GetAllUsersHandler getAllUsersHandler,
            GetUserHandler getUserHandler,
            UpdateUserHandler updateUserHandler,
            CreateUserHandler createUserHandler,
            LoginHandler loginHandler) {
        RequestPipeline pipeline = new DefaultRequestPipeline();

        pipeline
                .register(getAllUsersHandler)
                .register(getUserHandler)
                .register(createUserHandler)
                .register(updateUserHandler)
                .register(loginHandler);

        return pipeline;
    }
}
