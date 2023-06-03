package ma.ac.inpt.postservice.postMessaging;


import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.postservice.payload.PostEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@Slf4j
public class KafkaProducerConfig {

    @Bean
    public NewTopic commentTopic() {
        // Create a new Kafka topic named "post"
        return TopicBuilder.name("post")
                .build();
    }

    @Bean
    public ProducerFactory<String, PostEvent> producerFactory() {
        // Create a factory for producing Kafka messages with String keys and PostEvent values
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.TYPE_MAPPINGS, "PostEvent:ma.ac.inpt.postservice.payload.PostEvent");

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, PostEvent> kafkaTemplate() {
        // Create a KafkaTemplate for sending messages to the "post" topic
        return new KafkaTemplate<>(producerFactory());
    }

}
