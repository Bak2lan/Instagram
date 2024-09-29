package baktulan.instagram.api;
import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.userDTO.UserResponse;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.service.FollowerService;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/followers")
@RequiredArgsConstructor
public class FollowerAPI {
    private final FollowerService followerService;

    @PermitAll
    @GetMapping("/search")
    public List<ProfileUser> search (@RequestParam String name)throws NotFoundException{
        return followerService.search(name);
    }

    @PermitAll
    @GetMapping("{id}")
    public ProfileUser checkProfile(@PathVariable Long id) throws NotFoundException{
        return followerService.checkProfile(id);
    }

    @PermitAll
    @PostMapping("{id}")
    public ProfileUser subscribeToUser(@PathVariable Long id)throws NotFoundException{
        return followerService.subscribeToUser(id);
    }
    @PermitAll
    @PostMapping("/unSub/{id}")
    public ProfileUser unSubscribe(@PathVariable Long id) throws NotFoundException{
        return followerService.unSubscribe(id);
    }

    @PermitAll
    @GetMapping("/subscribers/{id}")
    public List<UserResponse>getAllSubscribersByUserId(@PathVariable Long id)throws NotFoundException{
        return followerService.getAllSubscribersByUserId(id);
    }

    @PermitAll
    @GetMapping("/subscriptions/{id}")
    public List<UserResponse>getAllSubscriptionsByUserId(@PathVariable Long id) throws NotFoundException{
        return followerService.getAllSubscriptionsByUserId(id);
    }

}
