package ma.ac.inpt.exceptions;

import ma.ac.inpt.models.Post;
import ma.ac.inpt.payload.PostEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;

public class KafkaMessageDeserializer
        extends ErrorHandlingDeserializer<PostEvent>
        implements Deserializer<PostEvent> {}