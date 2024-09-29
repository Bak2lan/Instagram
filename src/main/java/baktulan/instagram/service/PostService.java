package baktulan.instagram.service;

import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.postDTO.PostRequest;
import baktulan.instagram.dto.postDTO.PostResponse;
import baktulan.instagram.exception.AccessDeniedException;
import baktulan.instagram.exception.BadCredentialException;
import baktulan.instagram.exception.NotFoundException;

import java.util.List;

public interface PostService {

    PostResponse savePost(PostRequest postRequest) throws NotFoundException, BadCredentialException;
    PostResponse like(Long postId) throws NotFoundException;
    List<PostResponse>getAllPostByUserId(Long id) throws NotFoundException;
    SimpleResponse updatePost(Long id,PostRequest postRequest) throws NotFoundException, AccessDeniedException;
    SimpleResponse deletePost(Long id) throws NotFoundException, AccessDeniedException;

}
