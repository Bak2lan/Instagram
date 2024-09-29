package baktulan.instagram.service;

import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.imageDTO.ImageRequestForChange;
import baktulan.instagram.dto.userInfoDTO.UserInfoRequest;
import baktulan.instagram.dto.userInfoDTO.UserInfoResponse;
import baktulan.instagram.exception.AccessDeniedException;
import baktulan.instagram.exception.NotFoundException;

public interface UserInfoService {

    SimpleResponse saveUserInfoToUser(Long userId, UserInfoRequest userInfoRequest) throws NotFoundException, AccessDeniedException;
    UserInfoResponse getByUserId(Long userId) throws NotFoundException;
    SimpleResponse update(Long id,UserInfoRequest userInfoRequest) throws NotFoundException, AccessDeniedException;
    SimpleResponse changeImage(Long userInfoId, ImageRequestForChange imageRequestForChange) throws NotFoundException, AccessDeniedException;
    SimpleResponse deleteImage(Long userInfoId) throws NotFoundException, AccessDeniedException;
}
