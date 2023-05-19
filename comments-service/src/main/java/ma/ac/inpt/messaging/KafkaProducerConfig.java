package ma.ac.inpt.messaging;


import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.payload.CommentNumEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
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

    @Bean
    public ProducerFactory<String, CommentNumEvent> ProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(JsonSerializer.TYPE_MAPPINGS, "CommentNumEvent:ma.ac.inpt.payload.CommentNumEvent");


        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean()
    public KafkaTemplate<String, CommentNumEvent> KafkaTemplate() {
        return new KafkaTemplate<>(ProducerFactory());
    }


}