package ma.ac.inpt.feignClient;


import ma.ac.inpt.models.User;
import ma.ac.inpt.payload.PagedResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "GRAPH")
public interface Graph {

    @RequestMapping(method = RequestMethod.GET, value = "/users/paginated/{username}/followers")
    ResponseEntity<PagedResult<User>> findFollowers(
//            @RequestHeader("Authorization") String token,
            @PathVariable("username") String username,
            @RequestParam("page") int page,
            @RequestParam("size") int size);
}