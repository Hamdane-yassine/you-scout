package ma.ac.inpt.commentservice.messaging;


import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.commentservice.payload.CommentNumEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class KafkaProducerConfig {

    /**
     * The Kafka bootstrap servers address.
     */
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Returns the configuration properties for the Kafka producer.
     *
     * @return the configuration properties for the Kafka producer
     */
    public Map<String, Object> producerConfig() {
        HashMap<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }

    /**
     * Creates a new instance of the Kafka producer factory.
     *
     * @return the Kafka producer factory instance
     */
    @Bean
    public ProducerFactory<String, CommentNumEvent> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    /**
     * Creates a new instance of the Kafka template used for sending user events.
     *
     * @param producerFactory the Kafka producer factory
     * @return the Kafka template instance
     */
    @Bean
    public KafkaTemplate<String, CommentNumEvent> kafkaTemplate(ProducerFactory<String, CommentNumEvent> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }


}