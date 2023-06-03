
package ma.ac.inpt.commentservice.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;
@Configuration
public class KafkaTopicConfig {
    @Bean
    public NewTopic commentTopic(){
        return TopicBuilder.name("comment")
                .build();
    }
    }
