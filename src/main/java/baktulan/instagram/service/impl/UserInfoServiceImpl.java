package baktulan.instagram.service.impl;
import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.imageDTO.ImageRequestForChange;
import baktulan.instagram.dto.userInfoDTO.UserInfoRequest;
import baktulan.instagram.dto.userInfoDTO.UserInfoResponse;
import baktulan.instagram.entity.User;
import baktulan.instagram.entity.UserInfo;
import baktulan.instagram.exception.AccessDeniedException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.repository.UserInfoRepository;
import baktulan.instagram.repository.UserRepository;
import baktulan.instagram.securityConfig.JWTService;
import baktulan.instagram.service.UserInfoService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;
    private final JWTService jwtService;

    @Override
    public SimpleResponse saveUserInfoToUser(Long userId, UserInfoRequest userInfoRequest)throws NotFoundException,AccessDeniedException {
        ProfileUser profile = jwtService.getProfile();
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        if (!user.getEmail().equals(profile.getEmail())){
            throw new AccessDeniedException("You are not allowed to update another user's information");
        }
        UserInfo userInfo= user.getUserInfo();
        if (userInfo==null){
            userInfo= new UserInfo();
        }
        userInfo.setFullName(userInfoRequest.getFullName());
        userInfo.setBiography(userInfoRequest.getBiography());
        userInfo.setGender(userInfoRequest.getGender());
        userInfo.setImage(userInfoRequest.getImage());
        user.setUserInfo(userInfo);
        userInfoRepository.save(userInfo);
        userRepository.save(user);
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("UserInfo successfully saved to User :"+user.getEmail())
                .build();
    }

    @Override
    public UserInfoResponse getByUserId(Long userId) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Not found User"));
        return userInfoRepository.getByUserId(userId);
    }

    @Override
    public SimpleResponse update(Long id, UserInfoRequest userInfoRequest) throws NotFoundException ,AccessDeniedException{
        User user = userRepository.getUserByUserInfoId(id);
        if (user==null){
            throw new NotFoundException("User not found");
        }
        ProfileUser profile = jwtService.getProfile();
        UserInfo userInfo = userInfoRepository.findById(id).orElseThrow(() -> new NotFoundException("UserInfo not found"));
        if (!profile.getUserName().equals(user.getUsername())){
            throw new AccessDeniedException("You are not allowed to update another user's information");
        }
        userInfo.setFullName(userInfoRequest.getFullName());
        userInfo.setBiography(userInfoRequest.getBiography());
        userInfo.setGender(userInfoRequest.getGender());
        userInfo.setImage(userInfoRequest.getImage());

        userInfoRepository.save(userInfo);

        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("UserInfo successfully updated")
                .build();
    }

    @Override
    public SimpleResponse changeImage(Long userInfoId, ImageRequestForChange imageRequestForChange) throws NotFoundException,AccessDeniedException {
        User user = userRepository.getUserByUserInfoId(userInfoId);
        if (user==null){
            throw new NotFoundException("User not found");
        }
        ProfileUser profile = jwtService.getProfile();
        UserInfo userInfo = userInfoRepository.findById(userInfoId).orElseThrow(() -> new NotFoundException("UserInfo not found"));
        if (!profile.getUserName().equals(user.getUsername())){
            throw new AccessDeniedException("You are not allowed to update another user's information");
        }
            userInfo.setImage(imageRequestForChange.getImage());
            userInfoRepository.save(userInfo);
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("Image UserInfo changed")
                .build();
    }

    @Override
    public SimpleResponse deleteImage(Long userInfoId)throws NotFoundException ,AccessDeniedException {
        User user = userRepository.getUserByUserInfoId(userInfoId);
        if (user==null){
            throw new NotFoundException("User not found");
        }
        ProfileUser profile = jwtService.getProfile();
        UserInfo userInfo = userInfoRepository.findById(userInfoId).orElseThrow(() -> new NotFoundException("UserInfo not found"));
        if (!profile.getUserName().equals(user.getUsername())){
            throw new AccessDeniedException("You are not allowed to update another user's information");
        }
        userInfo.setImage(null);
        return SimpleResponse
                .builder()
                .httpStatus(HttpStatus.OK)
                .message("Image UserInfo deleted")
                .build();
    }
}
