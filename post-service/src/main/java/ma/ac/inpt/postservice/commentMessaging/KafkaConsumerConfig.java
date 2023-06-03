package ma.ac.inpt.postservice.commentMessaging;


import ma.ac.inpt.postservice.payload.CommentNumEvent;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class KafkaConsumerConfig {

    public Map<String, Object> consumerConfig() {
        // Create a HashMap to hold the configuration properties for the Kafka consumer
        HashMap<String, Object> configProps = new HashMap<>();

        // Set the bootstrap servers configuration property
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        // Set the deserializer classes for key and value
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        // Set the specific deserializer classes for key and value
        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());

        // Configure trusted packages for deserialization
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");

        // Configure type mappings for deserialization
        configProps.put(JsonSerializer.TYPE_MAPPINGS, "CommentNumEvent:ma.ac.inpt.postservice.payload.CommentNumEvent");

        return configProps;
    }

    @Bean
    public ConsumerFactory<String, CommentNumEvent> consumerFactory() {
        // Create a default Kafka consumer factory with the provided configuration
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, CommentNumEvent>> factory(ConsumerFactory<String, CommentNumEvent> consumerFactory) {
        // Create a concurrent Kafka listener container factory
        ConcurrentKafkaListenerContainerFactory<String, CommentNumEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

        // Set the consumer factory
        factory.setConsumerFactory(consumerFactory);

        return factory;
    }
}
