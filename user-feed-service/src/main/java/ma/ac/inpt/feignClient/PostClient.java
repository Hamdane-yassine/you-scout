package ma.ac.inpt.feignClient;


import ma.ac.inpt.payload.PostRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(name = "post-handling-service")
public interface PostClient {

    /**
     * Finds posts by IDs.
     *
     * @param ids the list of post IDs to search for
     * @return the ResponseEntity containing the list of posts
     */
    @RequestMapping(method = RequestMethod.POST, value = "/api/v1/post-handling-service/posts/in")
    ResponseEntity<List<PostRequest>> findPostsByIdIn(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody List<String> ids);
}
