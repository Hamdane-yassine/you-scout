
package ma.ac.inpt.commentservice.messaging;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.apache.kafka.clients.admin.NewTopic;
@Configuration
public class KafkaTopicConfig {
    /**
     * The name of the Kafka topic.
     */
    @Value("${spring.kafka.topic.name}")
    private String topicName;

    /**
     * Creates a new instance of the Kafka topic.
     *
     * @return the Kafka topic instance
     */
    @Bean
    public NewTopic usersTopic() {
        return TopicBuilder.name(topicName).build();
    }

}
