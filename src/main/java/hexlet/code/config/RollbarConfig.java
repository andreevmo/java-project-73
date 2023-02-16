package hexlet.code.config;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration()
@ComponentScan({
    "hexlet.code.app",
    "com.rollbar.spring"
})
public class RollbarConfig {

    /**
     * Register a Rollbar bean to configure App with Rollbar.
     */

    public static final String ROLLBAR_TOKEN = System.getenv("ROLLBAR_TOKEN");
    public static final String APP_ENV = System.getenv("APP_ENV");
    @Bean
    public Rollbar rollbar() {
        return new Rollbar(getRollbarConfigs());
    }

    private Config getRollbarConfigs() {
        return RollbarSpringConfigBuilder.withAccessToken(RollbarConfig.ROLLBAR_TOKEN)
                .environment(APP_ENV)
                .build();
    }
}
