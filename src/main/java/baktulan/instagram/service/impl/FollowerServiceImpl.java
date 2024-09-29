package baktulan.instagram.service.impl;
import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.imageDTO.ImageResponse;
import baktulan.instagram.dto.postDTO.PostResponse;
import baktulan.instagram.dto.userDTO.UserResponse;
import baktulan.instagram.entity.*;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.repository.FollowerRepository;
import baktulan.instagram.repository.UserRepository;
import baktulan.instagram.securityConfig.JWTService;
import baktulan.instagram.service.FollowerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final FollowerRepository followerRepository;

    @Override
    public List<ProfileUser> search(String name) throws NotFoundException {
        List<ProfileUser> search = followerRepository.search(name);
        if (search.isEmpty()){
            throw new NotFoundException("There is no users with this name");
        }else {
            return search;
        }
    }

    @Override
    public ProfileUser checkProfile(Long id) throws NotFoundException {
        ProfileUser profile = jwtService.getProfile();
        User currentUser = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        UserInfo userInfo = user.getUserInfo() != null ? user.getUserInfo() : new UserInfo();
        Follower follower = user.getFollower() != null ? user.getFollower() : new Follower();
        List<Post> posts = !user.getPosts().isEmpty() ? user.getPosts() : new ArrayList<>();

        int subscribers = follower.getSubscribers() != null ? follower.getSubscribers().size() : 0;
        int subscriptions = follower.getSubscriptions() != null ? follower.getSubscriptions().size() : 0;
        boolean isSubscribed = currentUser.getFollower() != null && currentUser.getFollower().getSubscriptions() != null &&
                               currentUser.getFollower().getSubscriptions().contains(user.getId());
        return ProfileUser
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
                .posts(posts)
                .isSubscribed(isSubscribed)
                .countSubscribers(subscribers)
                .countSubscriptions(subscriptions)
                .build();
    }

    @Override
    public ProfileUser subscribeToUser(Long id) throws NotFoundException {
        ProfileUser profile = jwtService.getProfile();
        User currentUser = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        ProfileUser profileUser = checkProfile(id);
        if (!profileUser.isSubscribed()) {
            currentUser.getFollower().getSubscriptions().add(user.getId());
            user.getFollower().getSubscribers().add(currentUser.getId());

            userRepository.save(currentUser);
            userRepository.save(user);

             profileUser.setMessage("You successfully subscribed");
             profileUser.setSubscribed(true);
        } else {
            profileUser.setMessage("You already subscribed to this User");

        }
        return profileUser;

    }

    @Override
    public ProfileUser unSubscribe(Long id)throws NotFoundException {
        ProfileUser profile = jwtService.getProfile();
        User currentUser = userRepository.findByEmail(profile.getEmail()).orElseThrow(() -> new NotFoundException("User not found"));
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        ProfileUser profileUser = checkProfile(id);
        if (profileUser.isSubscribed()) {
            currentUser.getFollower().getSubscriptions().remove(user.getId());
            user.getFollower().getSubscribers().remove(currentUser.getId());

            userRepository.save(currentUser);
            userRepository.save(user);

            profileUser.setMessage("You unsubscribed from this User");
            profileUser.setSubscribed(false);
        } else {
            profileUser.setMessage("You are not subscribed to this User");

        }
        return profileUser;

    }

    //TODO .builder()
    //                .userName(user.getUsername())
    //                .email(user.getEmail())
    //                .phoneNumber(user.getPhoneNumber())
    //                .fullName(userInfo.getFullName())
    //                .biography(userInfo.getBiography())
    //                .image(userInfo.getImage())
    //                .gender(userInfo.getGender())
    //                .subscribers(follower.getSubscribers())
    //                .subscription(follower.getSubscriptions())
    //                .posts(posts)
    //                .isSubscribed(isSubscribed)
    //                .countSubscribers(countOfSubscribers)
    //                .countSubscriptions(countOfSubscriptions)

    @Override
    public List<UserResponse> getAllSubscribersByUserId(Long id) throws NotFoundException {
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found User"));
        List<User> users = userRepository.findAll();
        List<UserResponse>userResponses= new ArrayList<>();

        List<Long> subscribers = user.getFollower().getSubscribers();
        for (User use:users){
            List<Post> posts = use.getPosts();
            boolean isSubscribed=profile.getSubscription().contains(use.getId());
            if (subscribers.contains(use.getId())){

                UserResponse userResponse= UserResponse
                        .builder()
                        .userName(use.getUsername())
                        .email(use.getEmail())
                        .phoneNumber(use.getPhoneNumber())
                        .fullName(use.getUserInfo()!=null&&use.getUserInfo().getFullName()!=null? use.getUserInfo().getFullName():null)
                        .biography(use.getUserInfo()!=null&&use.getUserInfo().getBiography()!=null? use.getUserInfo().getBiography():null)
                        .image(use.getUserInfo()!=null&&use.getUserInfo().getImage()!=null? use.getUserInfo().getImage():null)
                        .subscribers(use.getFollower()!=null&&use.getFollower().getSubscribers()!=null? use.getFollower().getSubscribers() : new ArrayList<>())
                        .subscription(use.getFollower()!=null&&use.getFollower().getSubscriptions()!=null? use.getFollower().getSubscriptions() : new ArrayList<>())
                        .posts(posts.stream().map(post -> PostResponse.builder()
                                .title(post.getTitle())
                                .description(post.getDescription())
                                .createdAt(post.getCreatedAt())
                                .imageResponses(post.getImages().stream().map(image -> new ImageResponse(image.getImageLink())).toList()).build()).toList())
                        .isSubscribed(isSubscribed)
                        .countSubscribers(use.getFollower()!=null&&use.getFollower().getSubscribers()!=null? use.getFollower().getSubscribers().size():0)
                        .countSubscriptions(use.getFollower()!=null&&use.getFollower().getSubscriptions()!=null? use.getFollower().getSubscriptions().size():0)
                        .build();
                userResponses.add(userResponse);
            }

        }
        return userResponses;
    }

    @Override
    public List<UserResponse> getAllSubscriptionsByUserId(Long id) throws NotFoundException {
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found User"));
        List<User> users = userRepository.findAll();
        List<UserResponse>userResponses= new ArrayList<>();

        List<Long> subscriptions = user.getFollower().getSubscriptions();
        for (User use:users){
            List<Post> posts = use.getPosts();
            boolean isSubscribed=profile.getSubscription().contains(use.getId());
            if (subscriptions.contains(use.getId())){
                UserResponse userResponse= UserResponse
                        .builder()
                        .userName(use.getUsername())
                        .email(use.getEmail())
                        .phoneNumber(use.getPhoneNumber())
                        .fullName(use.getUserInfo()!=null&&use.getUserInfo().getFullName()!=null? use.getUserInfo().getFullName():null)
                        .biography(use.getUserInfo()!=null&&use.getUserInfo().getBiography()!=null? use.getUserInfo().getBiography():null)
                        .image(use.getUserInfo()!=null&&use.getUserInfo().getImage()!=null? use.getUserInfo().getImage():null)
                        .subscribers(use.getFollower()!=null&&use.getFollower().getSubscribers()!=null? use.getFollower().getSubscribers() : new ArrayList<>())
                        .subscription(use.getFollower()!=null&&use.getFollower().getSubscriptions()!=null? use.getFollower().getSubscriptions() : new ArrayList<>())
                        .posts(posts.stream().map(post -> PostResponse.builder()
                                .title(post.getTitle())
                                .description(post.getDescription())
                                .createdAt(post.getCreatedAt())
                                .imageResponses(post.getImages().stream().map(image -> new ImageResponse(image.getImageLink())).toList()).build()).toList())
                        .isSubscribed(isSubscribed)
                        .countSubscribers(use.getFollower()!=null&&use.getFollower().getSubscribers()!=null? use.getFollower().getSubscribers().size():0)
                        .countSubscriptions(use.getFollower()!=null&&use.getFollower().getSubscriptions()!=null? use.getFollower().getSubscriptions().size():0)
                        .build();
                userResponses.add(userResponse);
            }

        }
        return userResponses;
    }
}
