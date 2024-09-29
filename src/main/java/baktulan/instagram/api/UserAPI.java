package baktulan.instagram.api;
import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.userDTO.PaginationResponse;
import baktulan.instagram.dto.userDTO.UserRequest;
import baktulan.instagram.dto.userDTO.UserResponse;
import baktulan.instagram.exception.AlreadyExistException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.service.AuthenticationService;
import baktulan.instagram.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAPI {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public SimpleResponse saveUser(@Valid @RequestBody UserRequest userRequest) throws AlreadyExistException {
        return userService.saveUser(userRequest);
    }

    @PermitAll
    @GetMapping("{id}")
    public UserResponse getById(@PathVariable Long id)throws NotFoundException{
        return userService.getUserById(id);
    }


    @PermitAll
    @GetMapping
    public List<UserResponse>getAll() throws NotFoundException{
        return userService.getAllUsers();
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("{id}")
    public SimpleResponse updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UserRequest userRequest)throws NotFoundException{
        return userService.updateUser(id,userRequest);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("{id}")
    public SimpleResponse deleteUser(@PathVariable Long id)throws NotFoundException{
        return userService.deleteUser(id);
    }

    @GetMapping("/pagination")
    public PaginationResponse getAllUsers(@RequestParam int currentPage,
                                          @RequestParam int pageSize){
        return userService.getAllUserPage(currentPage,pageSize);
    }


}
