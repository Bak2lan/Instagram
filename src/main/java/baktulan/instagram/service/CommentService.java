package baktulan.instagram.service;

import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.commentDTO.CommentRequest;
import baktulan.instagram.dto.commentDTO.CommentResponse;
import baktulan.instagram.dto.postDTO.PostResponse;
import baktulan.instagram.exception.AccessDeniedException;
import baktulan.instagram.exception.NotFoundException;

import java.util.List;

public interface CommentService {

    PostResponse saveCommentToPost(Long postId, CommentRequest commentRequest) throws NotFoundException;
    List<CommentResponse>findByPostId(Long postId) throws NotFoundException;
    CommentResponse likeToComment(Long commentId) throws NotFoundException;
    SimpleResponse deleteComment(Long commentId) throws NotFoundException, AccessDeniedException;

}
