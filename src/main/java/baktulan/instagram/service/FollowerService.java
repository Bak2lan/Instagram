package baktulan.instagram.service;

import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.userDTO.UserResponse;
import baktulan.instagram.exception.NotFoundException;

import java.util.List;

public interface FollowerService {

    List<ProfileUser>search(String name) throws NotFoundException;
    ProfileUser checkProfile(Long id) throws NotFoundException;
    ProfileUser subscribeToUser(Long id) throws NotFoundException;
    ProfileUser unSubscribe(Long id) throws NotFoundException;
    List<UserResponse> getAllSubscribersByUserId(Long id) throws NotFoundException;
    List<UserResponse> getAllSubscriptionsByUserId(Long id) throws NotFoundException;


}
