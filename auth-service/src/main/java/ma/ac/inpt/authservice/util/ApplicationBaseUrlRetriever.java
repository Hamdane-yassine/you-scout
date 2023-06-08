package ma.ac.inpt.authservice.util;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * A component responsible for retrieving the base URL of the application.
 * The base URL is composed of the application base URL and the server port number.
 */
@Component
public class ApplicationBaseUrlRetriever {

    /**
     * The application base URL.
     */
    @Value("${application.base-url}")
    private String baseUrl;

    /**
     * The server port number.
     */
    @Value("${server.servlet.context-path}")
    private String applicationContextPath;

    /**
     * Retrieves the base URL of the application by concatenating the application base URL and server port number.
     *
     * @return the base URL of the application
     */
    public String getBaseUrl() {
        return baseUrl + applicationContextPath;
    }
}

