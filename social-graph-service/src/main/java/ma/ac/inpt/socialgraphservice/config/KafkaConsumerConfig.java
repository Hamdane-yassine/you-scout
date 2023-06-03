package ma.ac.inpt.socialgraphservice.config;

import ma.ac.inpt.socialgraphservice.payload.UserEventPayload;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for Kafka consumer.
 */
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Returns the configuration properties for the Kafka consumer.
     *
     * @return the configuration properties for the Kafka consumer
     */
    public Map<String, Object> consumerConfig() {
        HashMap<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return configProps;
    }

    /**
     * Creates the Kafka consumer factory.
     *
     * @return the Kafka consumer factory
     */
    @Bean
    public ConsumerFactory<String, UserEventPayload> consumerFactory() {
        JsonDeserializer<UserEventPayload> jsonDeserializer = new JsonDeserializer<>(UserEventPayload.class);
        jsonDeserializer.addTrustedPackages("ma.ac.inpt");
        jsonDeserializer.ignoreTypeHeaders();
        return new DefaultKafkaConsumerFactory<>(
                consumerConfig(),
                new StringDeserializer(),
                jsonDeserializer
        );
    }

    /**
     * Creates the Kafka listener container factory.
     *
     * @param consumerFactory the Kafka consumer factory
     * @return the Kafka listener container factory
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, UserEventPayload>> factory(
            ConsumerFactory<String, UserEventPayload> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, UserEventPayload> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}

