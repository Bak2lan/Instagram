package baktulan.instagram.service.impl;

import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.commentDTO.CommentResponse;
import baktulan.instagram.dto.imageDTO.ImageResponse;
import baktulan.instagram.dto.likeDTO.LikeResponse;
import baktulan.instagram.dto.postDTO.PostResponse;
import baktulan.instagram.dto.userDTO.PaginationResponse;
import baktulan.instagram.dto.userDTO.UserRequest;
import baktulan.instagram.dto.userDTO.UserResponse;
import baktulan.instagram.dto.userDTO.UserResponseForComment;
import baktulan.instagram.entity.Follower;
import baktulan.instagram.entity.Post;
import baktulan.instagram.entity.User;
import baktulan.instagram.entity.UserInfo;
import baktulan.instagram.enums.Gender;
import baktulan.instagram.exception.AlreadyExistException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.repository.FollowerRepository;
import baktulan.instagram.repository.UserRepository;
import baktulan.instagram.securityConfig.JWTService;
import baktulan.instagram.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @Override
    public SimpleResponse saveUser(UserRequest userRequest) throws AlreadyExistException {
        if (userRepository.existsByEmail(userRequest.getEmail())){
           throw new AlreadyExistException("User with this email is already exist");
        }
        if (userRepository.existsByUsername(userRequest.getUserName())){
            throw new AlreadyExistException("User with this user name is already exist");
        }
        User user= new User();
        user.setUsername(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        Follower follower= new Follower();
        follower.setSubscribers(new ArrayList<>());
        follower.setSubscriptions(new ArrayList<>());

        user.setFollower(follower);
        followerRepository.save(follower);
        userRepository.save(user);

        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("User with email "+ userRequest.getEmail()+" is saved")
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) throws NotFoundException {
        ProfileUser profile = jwtService.getProfile();
        User currentUser = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("Not found user"));
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        UserInfo userInfo = user.getUserInfo() != null ? user.getUserInfo() : new UserInfo();
        Follower follower = user.getFollower() != null ? user.getFollower() : new Follower();
        List<Post> posts = user.getPosts() != null ? user.getPosts() : new ArrayList<>();
        boolean isSubscribed = (currentUser.getFollower().getSubscriptions().contains(user.getId()));
        int countOfSubscribers = follower.getSubscribers() != null ? follower.getSubscribers().size() : 0;
        int countOfSubscriptions = follower.getSubscriptions() != null ? follower.getSubscriptions().size() : 0;

        String message;
        if (isSubscribed){
            message="You are subscribed" ;
        }
        else {
            message="You are not subscribed";
        }
        return UserResponse
                .builder()
                .userName(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .fullName(userInfo.getFullName())
                .biography(userInfo.getBiography())
                .image(userInfo.getImage())
                .gender(userInfo.getGender())
                .subscribers(follower.getSubscribers())
                .subscription(follower.getSubscriptions())
                .posts(posts.stream().map(post -> PostResponse.builder()
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .createdAt(post.getCreatedAt())
                        .imageResponses(post.getImages().stream().map(image -> new ImageResponse(image.getImageLink())).toList())
                        .comments(post.getComments().stream()
                                .map(comment -> CommentResponse
                                        .builder()
                                        .comment(comment.getComment())
                                        .createdAt(comment.getCreatedAt())
                                        .countOfLikes(comment.getLikes()!=null ? comment.getLikes().size():0)
                                        .username(new UserResponseForComment(comment.getUser().getUsername())).build()).toList()
                        ).build()).toList())

                .isSubscribed(isSubscribed)
                .countSubscribers(countOfSubscribers)
                .countSubscriptions(countOfSubscriptions)
                .message(message)
                .build();
    }


    @Override
    public SimpleResponse deleteUser(Long id)throws NotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        userRepository.deleteById(user.getId());
        return SimpleResponse.
                builder()
                .httpStatus(HttpStatus.OK)
                .message("User successfully deleted")
                .build();
    }

    @Override
    public SimpleResponse updateUser(Long id, UserRequest userRequest)throws NotFoundException  {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        user.setUsername(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        userRepository.save(user);
        return SimpleResponse.
                builder()
                .httpStatus(HttpStatus.OK)
                .message("User successfully Updated")
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() throws NotFoundException{
        ProfileUser profile = jwtService.getProfile();
        List<User> all = userRepository.findAll();
        List<UserResponse> userResponses= new ArrayList<>();

        for (User user : all) {
            boolean isSubscribed = profile.getSubscription().contains(user.getId());
            String message;
            List<Post> posts = user.getPosts();
            if (isSubscribed){
                message="You are subscribed" ;
            }
            else {
                message="You are not subscribed";
            }

            UserInfo userInfo = user.getUserInfo();


            UserResponse userResponse = UserResponse.builder()
                    .userName(user.getUsername())
                    .email(user.getEmail())
                    .phoneNumber(user.getPhoneNumber())
                    .fullName(userInfo != null ? userInfo.getFullName() : null)
                    .biography(userInfo != null ? userInfo.getBiography() : null)
                    .gender(userInfo != null && userInfo.getGender() != null ? userInfo.getGender() : Gender.UNKNOWN)
                    .image(userInfo != null ? userInfo.getImage() : null)
                    .subscribers(user.getFollower().getSubscribers() != null ? user.getFollower().getSubscribers() : new ArrayList<>())
                    .subscription(user.getFollower().getSubscriptions() != null ? user.getFollower().getSubscriptions() : new ArrayList<>())
                    .posts(posts.stream().map(post -> PostResponse.builder()
                            .title(post.getTitle())
                            .description(post.getDescription())
                            .createdAt(post.getCreatedAt())
                            .imageResponses(post.getImages().stream().map(image -> new ImageResponse(image.getImageLink())).toList())
                            .comments(post.getComments().stream()
                                    .map(comment -> CommentResponse
                                            .builder()
                                            .comment(comment.getComment())
                                            .createdAt(comment.getCreatedAt())
                                            .countOfLikes(comment.getLikes()!=null ? comment.getLikes().size():0)
                                            .username(new UserResponseForComment(comment.getUser().getUsername())).build()).toList()
                            ).build()).toList())
                    .countSubscribers(user.getFollower().getSubscribers() != null ? user.getFollower().getSubscribers().size() : 0)
                    .countSubscriptions(user.getFollower().getSubscriptions() != null ? user.getFollower().getSubscriptions().size() : 0)
                    .isSubscribed(isSubscribed)
                    .message(message)
                    .build();

            userResponses.add(userResponse);
        }
        return userResponses;
    }

    @Override
    public PaginationResponse getAllUserPage(int currentPage, int pageSize) {
        Pageable pageable = PageRequest.of(currentPage-1, pageSize);
        Page<UserResponse> allUsers = userRepository.getAllUsers(pageable);

        return PaginationResponse
                .builder()
                .allUsers(allUsers.getContent())
                .currentPage(allUsers.getNumber()+1)
                .pageSize(allUsers.getTotalPages())
                .build();
    }
}
