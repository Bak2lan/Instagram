package baktulan.instagram.api;

import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.commentDTO.CommentRequest;
import baktulan.instagram.dto.commentDTO.CommentResponse;
import baktulan.instagram.dto.postDTO.PostResponse;
import baktulan.instagram.exception.AccessDeniedException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentAPI {
    private final CommentService commentService;

    @PostMapping("{id}")
    public PostResponse saveToPost(@PathVariable Long id,
                                   @RequestBody CommentRequest commentRequest) throws NotFoundException{
        return commentService.saveCommentToPost(id,commentRequest);
    }

    @GetMapping("{id}")
    public List<CommentResponse> getCommentsByPostId(@PathVariable Long id) throws NotFoundException{
        return commentService.findByPostId(id);
    }

    @PostMapping("/like/{id}")
    public CommentResponse likeToComment(@PathVariable Long id) throws NotFoundException{
        return commentService.likeToComment(id);
    }
    @DeleteMapping("{id}")
    public SimpleResponse deleteComment(@PathVariable Long id) throws NotFoundException, AccessDeniedException{
        return commentService.deleteComment(id);
    }

}
