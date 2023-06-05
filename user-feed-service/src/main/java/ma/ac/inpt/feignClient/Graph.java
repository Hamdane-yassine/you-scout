package ma.ac.inpt.feignClient;



import ma.ac.inpt.payload.PagedResult;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "SOCIAL-GRAPH-SERVICE")
public interface Graph {

    /**
     * Finds followers of a user.
     *
     * @param username the username of the user to find followers for
     * @param page     the page number of the results
     * @param size     the number of followers to retrieve per page
     * @return the ResponseEntity containing the paged result of followers
     */
    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/social-graph-service/users/{username}/followers")
    ResponseEntity<PagedResult<String>> findFollowers(
//            @RequestHeader("Authorization") String token,
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable("username") String username,
            @RequestParam("page") int page,
            @RequestParam("size") int size);


}