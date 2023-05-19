package ma.ac.inpt.authservice.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import ma.ac.inpt.authservice.dto.UserDetailsDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

/**
 * UserDetailsDtoSerializer is a custom JsonSerializer for UserDetailsDto.
 * It serializes the UserDetailsDto object into JSON format.
 * The purpose of this custom serializer is to conditionally include certain fields (isEnabled, roles)
 * in the JSON output based on the authority of the authenticated user.
 */
public class UserDetailsDtoSerializer extends JsonSerializer<UserDetailsDto> {

    /**
     * Serializes the UserDetailsDto object into JSON format.
     *
     * @param userDetailsDto     the UserDetailsDto object to serialize
     * @param jsonGenerator      the JsonGenerator used to generate the JSON output
     * @param serializerProvider the SerializerProvider
     * @throws IOException if there is an error writing the JSON output
     */
    @Override
    public void serialize(UserDetailsDto userDetailsDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("email", userDetailsDto.getEmail());
        jsonGenerator.writeStringField("username", userDetailsDto.getUsername());
        jsonGenerator.writeStringField("fullName", userDetailsDto.getFullName());
        jsonGenerator.writeStringField("profilePicture", userDetailsDto.getProfilePicture());
        jsonGenerator.writeObjectField("dateOfBirth", userDetailsDto.getDateOfBirth());
        jsonGenerator.writeStringField("gender", userDetailsDto.getGender());
        jsonGenerator.writeStringField("country", userDetailsDto.getCountry());
        jsonGenerator.writeStringField("cityOrRegion", userDetailsDto.getCityOrRegion());
        jsonGenerator.writeStringField("bio", userDetailsDto.getBio());
        jsonGenerator.writeObjectField("socialMediaLinks", userDetailsDto.getSocialMediaLinks());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SCOPE_ADMIN"))) {
            jsonGenerator.writeBooleanField("isEnabled", userDetailsDto.isEnabled());
            jsonGenerator.writeObjectField("roles", userDetailsDto.getRoles());
        }

        jsonGenerator.writeEndObject();
    }

    /**
     * Serializes the UserDetailsDto object with type information.
     * This method is called when the serializer is asked to include type information in the JSON output.
     *
     * @param value       the UserDetailsDto object to serialize
     * @param gen         the JsonGenerator used to generate the JSON output
     * @param serializers the SerializerProvider
     * @param typeSer     the TypeSerializer used to include type information
     * @throws IOException if there is an error writing the JSON output
     */
    @Override
    public void serializeWithType(UserDetailsDto value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
        typeSer.writeTypePrefix(gen, typeSer.typeId(value, JsonToken.START_OBJECT));
        serialize(value, gen, serializers);
        typeSer.writeTypeSuffix(gen, typeSer.typeId(value, JsonToken.END_OBJECT));
    }
}

