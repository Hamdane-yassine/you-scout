package ma.ac.inpt.authservice.util;

import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import ma.ac.inpt.authservice.exception.BaseUrlNotResolvedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationBaseUrlRetriever {

    private final EurekaClient eurekaClient;

    public String getApplicationBaseUrl() {
        return eurekaClient.getApplication("AUTH-SERVICE")
                .getInstances().stream()
                .findFirst().map(i -> "http://" + i.getHostName() + ":" + i.getPort())
                .orElseThrow(() -> new BaseUrlNotResolvedException("cannot resolve base url"));
    }

}



