package baktulan.instagram.service.impl;
import baktulan.instagram.dto.authenticationDTO.AuthenticationResponse;
import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.dto.authenticationDTO.SignInRequest;
import baktulan.instagram.dto.authenticationDTO.SignUpRequest;
import baktulan.instagram.dto.followerDTO.FollowerRequest;
import baktulan.instagram.entity.Follower;
import baktulan.instagram.entity.User;
import baktulan.instagram.exception.AlreadyExistException;
import baktulan.instagram.exception.BadCredentialException;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.repository.FollowerRepository;
import baktulan.instagram.repository.UserRepository;
import baktulan.instagram.securityConfig.JWTService;
import baktulan.instagram.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final FollowerRepository followerRepository;

    @Override
    public AuthenticationResponse signUp(SignUpRequest signUpRequest) throws AlreadyExistException {
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            throw new AlreadyExistException("User with email "+signUpRequest.getEmail()+" is already exist");
        }
        if (userRepository.existsByUsername(signUpRequest.getUserName())){
            throw new AlreadyExistException("User with user name "+signUpRequest.getUserName()+" is already exist");
        }
        User user= new User();
        user.setUsername(signUpRequest.getUserName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setRole(signUpRequest.getRole());

        Follower follower= new Follower();
        follower.setSubscriptions(new ArrayList<>());
        follower.setSubscribers(new ArrayList<>());
        user.setFollower(follower);
        followerRepository.save(follower);
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .email(user.getEmail())
                .token(token)
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthenticationResponse signIn(SignInRequest signInRequest) throws NotFoundException,BadCredentialException

    {
        User user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new NotFoundException("Not found User with this email"));
        if (!userRepository.existsByEmail(signInRequest.getEmail())){
            throw new NotFoundException("Not found User with this email");
        }
        if (signInRequest.getEmail().isBlank()){
            throw new BadCredentialException("Incorrect email");
        }
        if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())){
            throw new BadCredentialException("Incorrect password");
        }
        String token = jwtService.generateToken(user);
        return AuthenticationResponse
                .builder()
                .email(user.getEmail())
                .token(token)
                .role(user.getRole())
                .build();
    }
    @Override
    public ProfileUser getProfile()throws NotFoundException{
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        return ProfileUser
                .builder()
                .fullName(user.getUserInfo().getFullName())
                .image(user.getUserInfo().getImage())
                .email(user.getEmail())
                .subscribers(user.getFollower().getSubscribers())
                .subscription(user.getFollower().getSubscriptions())
                .posts(user.getPosts())
                .build();
    }


}
