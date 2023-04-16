package ma.ac.inpt.service;

import ma.ac.inpt.exceptions.UnableToGetPostsException;
import ma.ac.inpt.models.Post;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.feignClient.PostClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PostService {

    @Autowired
    private PostClient postServiceClient;


    public List<Post> findPostsIn( List<String> ids) {
        log.info("finding posts for ids {}", ids);

        ResponseEntity<List<Post>> response =
                postServiceClient.findPostsByIdIn(ids);

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new UnableToGetPostsException(
                    String.format("unable to get posts for ids", ids));
        }
    }
}