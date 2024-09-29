package baktulan.instagram.api;
import baktulan.instagram.dto.authenticationDTO.AuthenticationResponse;
import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.authenticationDTO.SignInRequest;
import baktulan.instagram.dto.authenticationDTO.SignUpRequest;
import baktulan.instagram.exception.AlreadyExistException;
import baktulan.instagram.exception.BadCredentialException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;







@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationAPI {
    private final AuthenticationService authenticationService;

    @PostMapping("/signUp")
    public AuthenticationResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) throws NotFoundException, AlreadyExistException {
        return authenticationService.signUp(signUpRequest);
    }
    @PostMapping("/signIn")
    public AuthenticationResponse signIn(@Valid @RequestBody SignInRequest signInRequest)throws NotFoundException, BadCredentialException{
        return authenticationService.signIn(signInRequest);
    }



}
