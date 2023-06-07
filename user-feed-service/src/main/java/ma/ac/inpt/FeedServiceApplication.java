package ma.ac.inpt;

import ma.ac.inpt.config.RsaKeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableBinding(PostEventStream.class)
@EnableFeignClients
@EnableConfigurationProperties(RsaKeysConfig.class)
//@EnableCassandraRepositories
public class FeedServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedServiceApplication.class, args);
    }

}