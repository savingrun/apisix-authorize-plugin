package run.saving.apisix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.boom.apisix", "org.apache.apisix.plugin.runner"})
public class ApisixAuthorizePluginApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApisixAuthorizePluginApplication.class, args);
    }

}
