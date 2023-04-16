package ma.ac.inpt.feignClient;


import ma.ac.inpt.models.User;
import ma.ac.inpt.payload.PagedResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "INSTA-GRAPH")
public interface Graph {

    @RequestMapping(method = RequestMethod.GET, value = "/users/paginated/{username}/followers")
    ResponseEntity<PagedResult<User>> findFollowers(
//            @RequestHeader("Authorization") String token,
            @PathVariable String username,
            @RequestParam int page,
            @RequestParam int size);
}