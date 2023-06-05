package ma.ac.inpt.feignClient;


import ma.ac.inpt.models.Post;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name = "POST")
public interface PostClient {

    /**
     * Finds posts by IDs.
     *
     * @param ids the list of post IDs to search for
     * @return the ResponseEntity containing the list of posts
     */
    @RequestMapping(method = RequestMethod.POST, value = "/posts/in")
    ResponseEntity<List<Post>> findPostsByIdIn(
//            @RequestHeader("Authorization") String token,
            @RequestBody List<String> ids);
}
