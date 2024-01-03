package vn.com.ocb.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.com.ocb.config.ApplicationComponent;
import vn.com.ocb.config.DaggerApplicationComponent;
import vn.com.ocb.pipeline.request.RequestPipeline;

@Configuration
public class SpringConfig {

    @Bean
    public ApplicationComponent getApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .build();
    }

    @Bean
    public RequestPipeline requestPipeline(ApplicationComponent applicationComponent) {
        return applicationComponent.requestPipeline();
    }
}
