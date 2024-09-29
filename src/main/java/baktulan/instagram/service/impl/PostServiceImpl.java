package baktulan.instagram.service.impl;

import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.commentDTO.CommentRequest;
import baktulan.instagram.dto.commentDTO.CommentResponse;
import baktulan.instagram.dto.imageDTO.ImageRequest;
import baktulan.instagram.dto.imageDTO.ImageResponse;
import baktulan.instagram.dto.likeDTO.LikeRequest;
import baktulan.instagram.dto.likeDTO.LikeResponse;
import baktulan.instagram.dto.postDTO.PostRequest;
import baktulan.instagram.dto.postDTO.PostResponse;
import baktulan.instagram.dto.userDTO.UserResponseForComment;
import baktulan.instagram.entity.*;
import baktulan.instagram.exception.AccessDeniedException;
import baktulan.instagram.exception.BadCredentialException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.repository.ImageRepository;
import baktulan.instagram.repository.LikeRepository;
import baktulan.instagram.repository.PostRepository;
import baktulan.instagram.repository.UserRepository;
import baktulan.instagram.securityConfig.JWTService;
import baktulan.instagram.service.PostService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final JWTService jwtService;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;

    @Override
    public PostResponse savePost(PostRequest postRequest) throws NotFoundException, BadCredentialException {
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("User Not found"));
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setDescription(postRequest.getDescription());
        post.setCreatedAt(postRequest.getCreateAt() != null ? postRequest.getCreateAt() : LocalDate.now());

        List<Like> likes = new ArrayList<>();
        post.setLikes(likes);

        if (postRequest.getImages() == null || postRequest.getImages().isEmpty()) {
            throw new BadCredentialException("Choose image!!!");
        }
        List<Image> images = new ArrayList<>();
        for (ImageRequest imageRequest : postRequest.getImages()) {
            if (imageRequest == null || imageRequest.getImageLink() == null) {
                throw new BadCredentialException("Choose image!!!");
            }

            Image image = new Image();
            image.setImageLink(imageRequest.getImageLink());
            images.add(image);
            image.setPost(post);
        }
        List<Comment> comments = new ArrayList<>();

        post.setImages(images);
        post.setUser(user);
        post.setComments(comments);

        postRepository.save(post);
        int countLikes = likes.size();

        List<LikeResponse> collect = likes.stream()
                .map(like -> new LikeResponse(like.isLike()))
                .toList();

        List<ImageResponse> imageResponses = post.getImages().stream()
                .map(image -> new ImageResponse(image.getImageLink()))
                .collect(Collectors.toList());

        List<CommentResponse> list = comments.stream()
                .map(comment -> CommentResponse.builder()
                        .comment(comment.getComment())
                        .createdAt(comment.getCreatedAt())
                        .username(new UserResponseForComment(comment.getUser().getUsername()))
                        .build()).toList();
        return new PostResponse(
                post.getTitle(),
                post.getDescription(),
                imageResponses,
                post.getCreatedAt(),
                list,
                countLikes


        );
    }

    @Override
    public PostResponse like(Long postId) throws NotFoundException {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post not found"));
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        boolean exist = likeRepository.existsLikeByUserAndPost(user.getId(), postId);
        System.out.println(exist);
        List<Like> likes = post.getLikes();
        if (!exist){
            Like like = new Like();
            like.setUser(user);
            like.setPost(post);
            likeRepository.save(like);
            likes.add(like);
        } else {
            Like like = likeRepository.findLikeByUserIdAndPostId(user.getId(), postId).orElseThrow(() -> new NotFoundException("Not Found Like"));
            likeRepository.delete(like);
            post.getLikes().remove(like);
        }
//        List<LikeResponse> list = likes.stream()
//                .map(changeLike -> new LikeResponse(changeLike.isLike()))
//                .toList();

        List<Image> images = post.getImages();
        List<ImageResponse> list1 = images.stream()
                .map(image -> new ImageResponse(image.getImageLink()))
                .toList();

        postRepository.save(post);
        return PostResponse
                .builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .imageResponses(list1)
                .createdAt(post.getCreatedAt())
                .comments(post.getComments().stream()
                        .map(comment -> CommentResponse.builder()
                                .comment(comment.getComment())
                                .createdAt(comment.getCreatedAt())
                                .countOfLikes(comment.getLikes().size())
                                .username(new UserResponseForComment(comment.getUser().getUsername()))
                                .build()).toList())
                .countOfLikes(likes.size())
                .build();
    }

    @Override
    public List<PostResponse> getAllPostByUserId(Long id) throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
        List<Post> posts = user.getPosts();
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(post -> PostResponse
                        .builder()
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .createdAt(post.getCreatedAt()!=null ? LocalDate.now():post.getCreatedAt())
                        .imageResponses(post.getImages().stream()
                                .map(image -> new ImageResponse(image.getImageLink())).toList())
                        .comments(post.getComments().stream().map(comment ->CommentResponse.builder().comment(comment.getComment()).createdAt(comment.getCreatedAt()).username(new UserResponseForComment(comment.getUser().getUsername())).countOfLikes(comment.getLikes().size()).build()).toList())
                        .countOfLikes(post.getLikes().size()).build()).toList();
    }

    @Override
    public SimpleResponse updatePost(Long id, PostRequest postRequest) throws NotFoundException, AccessDeniedException {
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("Not found User"));
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found post to update"));
        if (!post.getUser().getEmail().equals(user.getEmail())) {
            throw new AccessDeniedException(String.format("Only user with email %s can update this post", user.getEmail()));
        } else {
            post.setTitle(postRequest.getTitle());
            post.setDescription(postRequest.getDescription());
        }
        postRepository.save(post);
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("Post successfully updated")
                .build();
    }


    @Override
    public SimpleResponse deletePost(Long id) throws NotFoundException, AccessDeniedException {
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("Not found User"));
        Post post = postRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found post to update"));
        if (!post.getUser().getEmail().equals(user.getEmail())) {
            throw new AccessDeniedException(String.format("Only user with email %s can delete this post", user.getEmail()));
        } else {
            post.setUser(null);
            post.setLikes(null);
            post.setComments(null);
            postRepository.deleteById(post.getId());
        }
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("Post successfully deleted")
                .build();
    }


}

