
package ma.ac.inpt.commentservice.service;

import ma.ac.inpt.commentservice.exceptions.CommentException;
import ma.ac.inpt.commentservice.exceptions.PostException;
import ma.ac.inpt.commentservice.messaging.CommentEventSender;
import ma.ac.inpt.commentservice.model.Comment;
import ma.ac.inpt.commentservice.model.Post;
import ma.ac.inpt.commentservice.repository.CommentRepository;
import ma.ac.inpt.commentservice.repository.PostRepository;
import ma.ac.inpt.commentservice.repository.ReplyRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ReplyRepository replyRepository;
    private final CommentEventSender commentEventSender;

    public CommentService(CommentRepository commentRepository,
                          PostRepository postRepository,
                          ReplyRepository replyRepository, CommentEventSender commentEventSender) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.replyRepository = replyRepository;
        this.commentEventSender = commentEventSender;
    }

    public Comment createComment(String user, Comment comment){
        ObjectId objectId = new ObjectId();
        String ID = objectId.toHexString();

        List<String> replies = new ArrayList<>();
        List<String> likes = new ArrayList<>();

        Optional<Post> providedPost = postRepository.findById(comment.getPostId());
        Post post = providedPost.orElseThrow(() -> new PostException("Post not found"));

        List<String> postComments = post.getComments();
        postComments.add(ID);
        post.setComments(postComments);

        Comment newComment = new Comment(ID, user, comment.getBody(), replies, comment.getPostId(), LocalDateTime.now(), likes);
        commentEventSender.sendCommentNum(post);
        commentRepository.save(newComment);
        postRepository.save(post);
        return newComment;
    }


    public List<Comment> getAllCommentsForPost(String postId, String query) {
        Optional<Post> providedPost = postRepository.findById(postId);
        if (providedPost.isEmpty()){
            throw new PostException("Post not found");
        }
        if (query.equals("oldest")) {
            Optional<List<Comment>> providedComments = commentRepository.findCommentsByPostIdOrderByTimestampAsc(postId);
            return providedComments.orElseThrow(() -> new CommentException("Comments not found"));
        }
        if (query.equals("recent")) {
            Optional<List<Comment>> providedComments = commentRepository.findCommentsByPostIdOrderByTimestampDesc(postId);
            return providedComments.orElseThrow(() -> new CommentException("Comments not found"));
        }
        if (query.equals("popularity")) {
            Optional<List<Comment>> providedComments = commentRepository.findCommentsByPostIdOrderByLikesDesc(postId);
            return providedComments.orElseThrow(() -> new CommentException("Comments not found"));
        }
        return checkPost(postId);
    }

    public Comment getLastCommentForPost(String postId) {
        Optional<Post> providedPost = postRepository.findById(postId);
        if (providedPost.isEmpty()){
            throw new PostException("Post not found");
        }
        List<Comment> comments = checkPost(postId);
        return comments.get(comments.size() - 1);
    }

    private List<Comment> checkPost(String postId) {
        Optional<List<Comment>> providedComments = commentRepository.findCommentsByPostId(postId);
        return providedComments.orElseThrow(() -> new CommentException("Comments not found"));
    }

    public Comment getComment(String id) {
        Optional<Comment> providedComment = commentRepository.findById(id);
        return providedComment.orElseThrow(() -> new CommentException("Comment not found"));
    }

    public String likeComment(String id, String user) {
        Optional<Comment> providedComment = commentRepository.findById(id);
        Comment comment = providedComment.orElseThrow(() -> new CommentException("Comment not found"));

        List<String> commentLikes = comment.getLikes();
        if (!commentLikes.contains(user)) {
            commentLikes.add(user);
            comment.setLikes(commentLikes);
            commentRepository.save(comment);
            return "Comment liked successfully";
        } else {
            return "Comment already liked!";
        }
    }

    public String unlikeComment(String id, String user) {
        Optional<Comment> providedComment = commentRepository.findById(id);
        Comment comment = providedComment.orElseThrow(() -> new CommentException("Comment not found"));

        List<String> commentLikes = comment.getLikes();
        if (commentLikes.contains(user)) {
            commentLikes.remove(user);
            comment.setLikes(commentLikes);
            commentRepository.save(comment);
            return "Comment unliked successfully";
        } else {
            return "Comment was not liked!";
        }
    }

    public String updateComment(String id, Comment newComment) {
        Optional<Comment> providedComment = commentRepository.findById(id);
        Comment comment = providedComment.orElseThrow(() -> new CommentException("Comment not found"));

        comment.setBody(newComment.getBody());
        commentRepository.save(comment);
        return "Comment updated successfully";
    }

    public String deleteComment(String id) {
        Optional<Comment> providedComment = commentRepository.findById(id);
        Comment comment = providedComment.orElseThrow(() -> new CommentException("Comment not found"));

        Optional<Post> providedPost = postRepository.findById(comment.getPostId());
        Post post = providedPost.orElseThrow(() -> new PostException("Post not found"));

        // Delete the deleted comment's id from the post's comments List
        List<String> newComments = post.getComments();
        newComments.remove(id);
        post.setComments(newComments);
        postRepository.save(post);

        // Delete all the replies inside the replies list of the comment
        replyRepository.deleteRepliesByRepliedTo(id);

        commentRepository.deleteById(id);
        return "Comment deleted!";
    }
}
