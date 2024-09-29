package baktulan.instagram.api;
import baktulan.instagram.dto.SimpleResponse;
import baktulan.instagram.dto.imageDTO.ImageRequestForChange;
import baktulan.instagram.dto.userInfoDTO.UserInfoRequest;
import baktulan.instagram.dto.userInfoDTO.UserInfoResponse;
import baktulan.instagram.exception.AccessDeniedException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.service.UserInfoService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userInfos")
@RequiredArgsConstructor
public class UserInfoAPI {
    private final UserInfoService userInfoService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("{id}")
    public SimpleResponse saveUserInfo(@PathVariable Long id,
                                       @RequestBody UserInfoRequest userInfoRequest) throws NotFoundException, AccessDeniedException {
        return userInfoService.saveUserInfoToUser(id,userInfoRequest);
    }

    @PermitAll
    @GetMapping("{id}")
    public UserInfoResponse getByUserId(@PathVariable Long id) throws NotFoundException{
        return userInfoService.getByUserId(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("{id}")
    public SimpleResponse updateUserInfo(@PathVariable Long id,
                                         @RequestBody UserInfoRequest userInfoRequest)throws NotFoundException,AccessDeniedException{
        return userInfoService.update(id,userInfoRequest);
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/change/{id}")
    public SimpleResponse changeImage(@PathVariable Long id,
                                      @RequestBody ImageRequestForChange imageRequestForChange)throws NotFoundException,AccessDeniedException{
        return userInfoService.changeImage(id,imageRequestForChange);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping ("/deleteImage/{id}")
    public SimpleResponse deleteImage (@PathVariable Long id) throws NotFoundException,AccessDeniedException{
        return userInfoService.deleteImage(id);
    }
}
