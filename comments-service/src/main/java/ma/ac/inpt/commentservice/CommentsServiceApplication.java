package ma.ac.inpt.commentservice;

import ma.ac.inpt.commentservice.config.RsaKeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysConfig.class)
public class CommentsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentsServiceApplication.class, args);
	}

}