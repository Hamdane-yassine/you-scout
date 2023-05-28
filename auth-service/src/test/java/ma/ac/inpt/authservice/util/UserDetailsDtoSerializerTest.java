package ma.ac.inpt.authservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import ma.ac.inpt.authservice.dto.UserDetailsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("UserDetailsDtoSerializer Test")
class UserDetailsDtoSerializerTest {

    ObjectMapper mapper = new ObjectMapper();

    UserDetailsDtoSerializer serializer = new UserDetailsDtoSerializer();

    Authentication authentication = Mockito.mock(Authentication.class);

    public UserDetailsDtoSerializerTest() {
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("Should serialize UserDetailsDto correctly")
    void serialize() throws JsonProcessingException {
        //given
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        mapper.registerModule(javaTimeModule);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        Map<String, String> socialMediaLinks = new HashMap<>();
        socialMediaLinks.put("Facebook", "https://facebook.com/testUser");

        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .email("testUser@email.com")
                .username("testUser")
                .fullName("Test User")
                .profilePicture("testProfilePic.jpg")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .gender("Male")
                .country("Test Country")
                .cityOrRegion("Test City")
                .bio("This is a test user")
                .socialMediaLinks(socialMediaLinks)
                .isEnabled(true)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        when(authentication.getAuthorities()).thenReturn(Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Register the serializer module
        SimpleModule module = new SimpleModule();
        module.addSerializer(UserDetailsDto.class, serializer);
        mapper.registerModule(module);

        // when
        String serializedJson = mapper.writeValueAsString(userDetailsDto);

        // then
        String expected = "{\"email\":\"testUser@email.com\",\"username\":\"testUser\",\"fullName\":\"Test User\",\"profilePicture\":\"testProfilePic.jpg\",\"dateOfBirth\":\"2000-01-01\",\"gender\":\"Male\",\"country\":\"Test Country\",\"cityOrRegion\":\"Test City\",\"bio\":\"This is a test user\",\"socialMediaLinks\":{\"Facebook\":\"https://facebook.com/testUser\"}}";
        assertEquals(expected, serializedJson);
    }
}


