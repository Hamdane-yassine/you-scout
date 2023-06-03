package ma.ac.inpt.feignClient;


import ma.ac.inpt.payload.UserSummary;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name = "AUTH")
public interface AuthClient {

    /**
     * Finds user summaries by usernames.
     *
     * @param usernames the list of usernames to search for
     * @return the ResponseEntity containing the list of user summaries
     */
    @RequestMapping(method = RequestMethod.POST, value = "/users/summary/in")
    ResponseEntity<List<UserSummary>> findByUsernameIn(
//            @RequestHeader("Authorization") String token,
            @RequestBody List<String> usernames);
}
