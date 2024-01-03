package vn.com.ocb.config;

import dagger.BindsInstance;
import dagger.Component;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.sqlclient.Pool;
import vn.com.ocb.pipeline.request.RequestPipeline;
import vn.com.ocb.rest.constant.config.Bootstrap;

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

    RequestPipeline requestPipeline();

}
