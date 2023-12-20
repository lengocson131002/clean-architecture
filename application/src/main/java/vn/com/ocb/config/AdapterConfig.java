package vn.com.ocb.config;

import dagger.Binds;
import dagger.Module;
import vn.com.ocb.adapter.IdGeneratorImpl;
import vn.com.ocb.adapter.PasswordEncoderImpl;
import vn.com.ocb.port.IdGenerator;
import vn.com.ocb.port.PasswordEncoder;

import javax.inject.Singleton;

@Module(includes = {
        AdapterConfig.IdGeneratorModule.class,
        AdapterConfig.PasswordEncoderModule.class
})
public final class AdapterConfig {

    // Used for @Bind dependencies
    @Module
    interface IdGeneratorModule {
        @Binds
        @Singleton
        IdGenerator bindIdGenerator(IdGeneratorImpl idGenerator);
    }

    @Module
    interface PasswordEncoderModule {
        @Binds
        @Singleton
        PasswordEncoder bindPasswordEncoder(PasswordEncoderImpl passwordEncoder);
    }
    // Other @Provides dependencies

}
