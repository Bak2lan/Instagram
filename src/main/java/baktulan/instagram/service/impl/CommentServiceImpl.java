package baktulan.instagram.service.impl;

import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.commentDTO.CommentRequest;
import baktulan.instagram.dto.commentDTO.CommentResponse;
import baktulan.instagram.dto.imageDTO.ImageResponse;
import baktulan.instagram.dto.likeDTO.LikeRequest;
import baktulan.instagram.dto.postDTO.PostResponse;
import baktulan.instagram.dto.userDTO.UserResponseForComment;
import baktulan.instagram.entity.Comment;
import baktulan.instagram.entity.Like;
import baktulan.instagram.entity.Post;
import baktulan.instagram.entity.User;
import baktulan.instagram.exception.AccessDeniedException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.repository.CommentRepository;
import baktulan.instagram.repository.LikeRepository;
import baktulan.instagram.repository.PostRepository;
import baktulan.instagram.repository.UserRepository;
import baktulan.instagram.securityConfig.JWTService;
import baktulan.instagram.service.CommentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    @Override
    public PostResponse saveCommentToPost(Long postId, CommentRequest commentRequest) throws NotFoundException {
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not found post"));
        Comment comment = new Comment();
        comment.setComment(commentRequest.getComment());

        comment.setCreatedAt(LocalDate.now());

        List<Like>likes= new ArrayList<>();
        comment.setLikes(likes);
        comment.setUser(user);
        comment.setPost(post);

        if (post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }
        post.getComments().add(comment);
        postRepository.save(post);


        return PostResponse
                .builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .createdAt(post.getCreatedAt())
                .countOfLikes(post.getLikes().size())
                .imageResponses(post.getImages().stream()
                        .map(image -> new ImageResponse(image.getImageLink())).toList())
                .comments(post.getComments().stream()
                        .map(comment1 -> CommentResponse.builder()
                                .comment(comment1.getComment())
                                .createdAt(comment1.getCreatedAt()!=null? comment1.getCreatedAt(): LocalDate.now())
                                .countOfLikes(comment1.getLikes().size())
                                .username(new UserResponseForComment(comment1.getUser().getUsername()))
                                .build()).toList()).build();
    }

    @Override
    public List<CommentResponse> findByPostId(Long postId) throws NotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Not found post"));
        List<Comment> comments = post.getComments();
        return  comments.stream()
                .map(comment -> CommentResponse
                        .builder()
                        .comment(comment.getComment())
                        .createdAt(comment.getCreatedAt())
                        .username(new UserResponseForComment(comment.getUser().getUsername()))
                        .countOfLikes(comment.getLikes()!=null ? comment.getLikes().size():0)
                        .build())
                .toList();
    }

    @Override
    public CommentResponse likeToComment(Long commentId) throws NotFoundException {
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Not found comment"));
        boolean b = commentRepository.existLikeToComment(user.getId(), commentId);

        List<Like>likes= comment.getLikes();
        if (!b){
            Like like= new Like();
            like.setUser(user);
            like.setComment(comment);
            likes.add(like);
            likeRepository.save(like);
            comment.getPost().getLikes().add(like);
        }
        else {
            Like like = likeRepository.findLikeByUserAndCommentId(user.getId(), commentId).orElseThrow(() -> new NotFoundException("Not Found Like"));
            likeRepository.deleteById(like.getId());
            comment.getPost().getLikes().remove(like);
            likes.remove(like);


        }

        comment.setLikes(likes);
        commentRepository.save(comment);

        return CommentResponse
                .builder()
                .comment(comment.getComment())
                .createdAt(comment.getCreatedAt())
                .username(new UserResponseForComment(comment.getUser().getUsername()))
                .countOfLikes(comment.getLikes()!=null ? comment.getLikes().size() : 0)
                .build();
    }

    @Override
    public SimpleResponse deleteComment(Long commentId) throws NotFoundException ,AccessDeniedException{
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Comment not found"));
        if (comment.getUser().getEmail().equals(user.getEmail())) {
            comment.setLikes(null);
            commentRepository.deleteById(comment.getId());
        }else{
            throw new AccessDeniedException("You can not delete other user`s comments");
        }
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("Comment Successfully deleted")
                .build();
    }
}
