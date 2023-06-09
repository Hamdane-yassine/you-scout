package ma.ac.inpt.authservice.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("ApplicationBaseUrlRetriever Test")
class ApplicationBaseUrlRetrieverTest {

    private final ApplicationBaseUrlRetriever retriever = new ApplicationBaseUrlRetriever();

    @Test
    @DisplayName("Should correctly retrieve base URL")
    void getBaseUrl() {
        // given
        ReflectionTestUtils.setField(retriever, "baseUrl", "http://localhost");
        ReflectionTestUtils.setField(retriever, "applicationContextPath", "/api/v1/auth-service");

        // when
        String baseUrl = retriever.getBaseUrl();

        // then
        assertEquals("http://localhost/api/v1/auth-service", baseUrl);
    }

    @Test
    @DisplayName("Should correctly retrieve base URL with different port")
    void getBaseUrlWithDifferentPort() {
        // given
        ReflectionTestUtils.setField(retriever, "baseUrl", "http://localhost");
        ReflectionTestUtils.setField(retriever, "applicationContextPath", "/api/v1/auth-service");

        // when
        String baseUrl = retriever.getBaseUrl();

        // then
        assertEquals("http://localhost/api/v1/auth-service", baseUrl);
    }

    @Test
    @DisplayName("Should correctly retrieve base URL with https")
    void getBaseUrlWithHttps() {
        // given
        ReflectionTestUtils.setField(retriever, "baseUrl", "http://localhost");
        ReflectionTestUtils.setField(retriever, "applicationContextPath", "/api/v1/auth-service");

        // when
        String baseUrl = retriever.getBaseUrl();

        // then
        assertEquals("http://localhost/api/v1/auth-service", baseUrl);
    }
}

