package ma.ac.inpt.apigateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // Make sure this is active during tests
public class ApiGatewayTest {

    private WireMockServer wireMockServer;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private WebClient webClient;

    @MockBean
    private ReactiveJwtDecoder jwtDecoder;

    @BeforeEach
    public void setup() {
        // Start a WireMock server with a dynamic port
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();

        // Create a WebClient with the base URL set to the WireMock server's port
        webClient = webClientBuilder.baseUrl("http://localhost:" + wireMockServer.port()).build();

        // Configure the mock JWT decoder to return a predefined JWT when decoding
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", "subject")
                .build();
        when(jwtDecoder.decode(anyString())).thenReturn(Mono.just(jwt));

        // Stubbing for any GET request to /api/v1/auth/test
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/auth/test"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, "text/plain")
                        .withStatus(200)
                        .withBody("Response from Auth Service")));
    }

    @Test
    public void authRouteWorks() {
        // Send a GET request to /api/v1/auth/test using the WebClient
        String responseBody = webClient.get().uri("/api/v1/auth/test")
                .exchangeToMono(clientResponse -> {
                    // Verify that the response status is OK
                    assertEquals(HttpStatus.OK, clientResponse.statusCode());

                    // Extract the response body
                    return clientResponse.bodyToMono(String.class);
                })
                .block();

        // Verify the response body content
        assertEquals("Response from Auth Service", responseBody);
    }

    @AfterEach
    public void tearDown() {
        // Stop the WireMock server after each test
        wireMockServer.stop();
    }
}