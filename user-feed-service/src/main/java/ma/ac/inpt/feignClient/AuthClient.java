package ma.ac.inpt.feignClient;

//
//import com.clone.instagram.instafeedservice.payload.JwtAuthenticationResponse;
//import com.clone.instagram.instafeedservice.payload.ServiceLoginRequest;
import ma.ac.inpt.payload.UserSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name = "INSTA-AUTH")
public interface AuthClient {

//    @RequestMapping(method = RequestMethod.POST, value = "signin")
//    ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody ServiceLoginRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/users/summary/in")
    ResponseEntity<List<UserSummary>> findByUsernameIn(
//            @RequestHeader("Authorization") String token,
            @RequestBody List<String> usernames);
}