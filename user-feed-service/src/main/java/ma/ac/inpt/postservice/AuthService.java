package ma.ac.inpt.postservice;


import lombok.RequiredArgsConstructor;
import ma.ac.inpt.exceptions.UnableToGetUsersException;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.feignClient.AuthClient;
import ma.ac.inpt.payload.UserSummary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toMap;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthClient authClient;
//    @Autowired private ServiceLoginRequest serviceLoginRequest;

//    public String getAccessToken() {
//
//        ResponseEntity<JwtAuthenticationResponse> response =
//                authClient.signin(serviceLoginRequest);
//
//        if(!response.getStatusCode().is2xxSuccessful()) {
//            String message = String.format("unable to get access token for service account, %s",
//                    response.getStatusCode());
//
//            log.error(message);
//            throw new UnableToGetAccessTokenException(message);
//        }
//
//        return response.getBody().getAccessToken();
//    }

    public Map<String, String> usersProfilePic(
                                               List<String> usernames) {

        ResponseEntity<List<UserSummary>> response =
                authClient.findByUsernameIn( usernames);

        if(!response.getStatusCode().is2xxSuccessful()) {
            String message = String.format("unable to get user summaries %s",
                    response.getStatusCode());

            log.error(message);
            throw new UnableToGetUsersException(message);
        }

        return Objects.requireNonNull(response
                        .getBody())
                        .stream()
                        .collect(toMap(UserSummary::getUsername,
                                UserSummary::getProfilePicture));
    }
}