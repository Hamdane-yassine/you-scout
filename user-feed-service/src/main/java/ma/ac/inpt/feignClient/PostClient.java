package ma.ac.inpt.feignClient;


//import com.clone.instagram.instafeedservice.payload.JwtAuthenticationResponse;
//import com.clone.instagram.instafeedservice.payload.ServiceLoginRequest;
import ma.ac.inpt.models.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name = "INSTA-POST")
public interface PostClient {

    @RequestMapping(method = RequestMethod.POST, value = "/posts/in")
    ResponseEntity<List<Post>> findPostsByIdIn(
//            @RequestHeader("Authorization") String token,
            @RequestBody List<String> ids);

}