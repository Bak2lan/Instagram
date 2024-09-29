package baktulan.instagram.api;


import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.postDTO.PostRequest;
import baktulan.instagram.dto.postDTO.PostResponse;
import baktulan.instagram.exception.AccessDeniedException;
import baktulan.instagram.exception.BadCredentialException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.service.PostService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostAPI {
    private final PostService postService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public PostResponse savePost(@RequestBody PostRequest postRequest) throws NotFoundException, BadCredentialException {
        return postService.savePost(postRequest);
    }


    @PermitAll
    @PostMapping("/like/{id}")
    public PostResponse likePost(@PathVariable Long id) throws NotFoundException {
        return postService.like(id);
    }


    @GetMapping("{id}")
    public List<PostResponse> getAllPostsByUserId(@PathVariable Long id) throws NotFoundException {
        return postService.getAllPostByUserId(id);
    }


    @PutMapping("{id}")
    public SimpleResponse updatePost(@PathVariable Long id,
                                     @RequestBody PostRequest postRequest) throws NotFoundException, AccessDeniedException {
        return postService.updatePost(id, postRequest);
    }

    @DeleteMapping("{id}")
    public SimpleResponse deletePost(@PathVariable Long id) throws NotFoundException, AccessDeniedException {
        return postService.deletePost(id);
    }
}
