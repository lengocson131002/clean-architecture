package vn.com.ocb.config;

import dagger.BindsInstance;
import dagger.Component;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.sqlclient.Pool;
import vn.com.ocb.adapter.config.KafkaRequestHandler;
import vn.com.rest.config.Bootstrap;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
        CoreConfig.class,
        DataConfig.class ,
        AdapterConfig.class,
        PresentationConfig.class,
})
public interface ApplicationComponent {

    Bootstrap boot();

    Pool pool();

    /**
     * Builder for the main dagger component.
     */
    @Component.Builder
    interface Builder {

        /**
         * Inject the options.
         *
         * @param options
         * @return
         */
        @BindsInstance
        Builder configuration(HttpServerOptions options);

        /**
         * Build the component.
         *
         * @return
         */
        ApplicationComponent build();
    }
}
