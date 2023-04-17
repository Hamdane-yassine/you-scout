package ma.ac.inpt;

//import ma.ac.inpt.connection.datastax_astra_configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;

@SpringBootApplication
//@EnableBinding(PostEventStream.class)
@EnableFeignClients
//@EnableCassandraRepositories
public class InstaFeedServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InstaFeedServiceApplication.class, args);
    }

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
}
//    @Bean
//    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(datastax_astra_configuration astraProperties) {
//        Path bundle = astraProperties.getSecureConnectionBundle().toPath();
//        return builder -> builder.withCloudSecureConnectBundle(bundle);
//    }
//}