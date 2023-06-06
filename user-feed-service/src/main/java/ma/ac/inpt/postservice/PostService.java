package ma.ac.inpt.postservice;

import lombok.RequiredArgsConstructor;
import ma.ac.inpt.exceptions.UnableToGetPostsException;
import ma.ac.inpt.models.Post;
import lombok.extern.slf4j.Slf4j;
import ma.ac.inpt.feignClient.PostClient;
import ma.ac.inpt.payload.PostRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

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
    public List<Post> findPostsIn(List<String> ids, String accessToken) {
        log.info("finding posts for ids {}", ids);

        ResponseEntity<List<PostRequest>> response =
                postServiceClient.findPostsByIdIn(accessToken, ids );

        if (response.getStatusCode().is2xxSuccessful()) {

            return convertTo(Objects.requireNonNull(response.getBody()));
        } else {
            throw new UnableToGetPostsException(
                    String.format("unable to get posts for ids: %s", ids));
        }
    }

    private List<Post> convertTo(List<PostRequest> payloads) {
        List<Post> posts = new ArrayList<>();

        for (PostRequest payload : payloads) {
            Post post = Post.builder()
                    ._id(payload.get_id())
                    .createdAt(payload.getCreatedAt())
                    .username(payload.getUsername())
                    .userProfilePic(payload.getUserProfilePic())
                    .likes(payload.getLikes())
                    .commentsNum(payload.getCommentsNum())
                    .skills(payload.getSkills())
                    .videoUrl(payload.getVideoUrl())
                    .build();

            posts.add(post);
        }

        return posts;
    }

}
