package ma.ac.inpt.postservice;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.exceptions.UnableToGetPostsException;
import ma.ac.inpt.models.Post;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.feignClient.PostClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostClient postServiceClient; // The PostClient used for retrieving posts

    /**
     * Retrieves posts based on the provided IDs.
     *
     * @param ids The list of post IDs to retrieve.
     * @return A list of posts.
     * @throws UnableToGetPostsException If unable to retrieve posts.
     */
    public List<Post> findPostsIn(List<String> ids) {
        log.info("finding posts for ids {}", ids);

        ResponseEntity<List<Post>> response =
                postServiceClient.findPostsByIdIn(ids);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new UnableToGetPostsException(
                    String.format("unable to get posts for ids: %s", ids));
        }
    }
}
