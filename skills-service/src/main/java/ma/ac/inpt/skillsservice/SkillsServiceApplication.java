package ma.ac.inpt.skillsservice;

import ma.ac.inpt.skillsservice.config.RsaKeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysConfig.class)
public class SkillsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillsServiceApplication.class, args);
	}

}
