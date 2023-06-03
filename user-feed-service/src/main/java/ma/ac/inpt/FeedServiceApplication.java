package ma.ac.inpt;

//import ma.ac.inpt.connection.datastax_astra_configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableBinding(PostEventStream.class)
@EnableFeignClients
//@EnableCassandraRepositories
public class FeedServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedServiceApplication.class, args);
    }

}