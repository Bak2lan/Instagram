package baktulan.instagram.service;

import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.userDTO.PaginationResponse;
import baktulan.instagram.dto.userDTO.UserRequest;
import baktulan.instagram.dto.userDTO.UserResponse;
import baktulan.instagram.exception.AlreadyExistException;
import baktulan.instagram.exception.NotFoundException;

import java.util.List;

public interface UserService {
    SimpleResponse saveUser(UserRequest userRequest) throws AlreadyExistException;
    UserResponse getUserById(Long id) throws NotFoundException;
    SimpleResponse deleteUser(Long id) throws NotFoundException;
    SimpleResponse updateUser(Long id, UserRequest userRequest) throws NotFoundException;
    List<UserResponse>getAllUsers() throws NotFoundException;
    PaginationResponse getAllUserPage(int currentPage,int pageSize);
}
