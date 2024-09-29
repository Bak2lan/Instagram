package baktulan.instagram.securityConfig;

import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.entity.Post;
import baktulan.instagram.entity.User;
import baktulan.instagram.entity.UserInfo;
import baktulan.instagram.enums.Gender;
import baktulan.instagram.exception.NotFoundException;
import baktulan.instagram.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JWTService {

    private final UserRepository userRepository;
    @Value("${jwt.secret.Key}")
    private String secretKey;
public String generateToken(User user){
       return JWT.create()
                .withClaim("email",user.getEmail())
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(ZonedDateTime.now().plusDays(10).toInstant()))
                .sign(Algorithm.HMAC256(secretKey));

    }

    public String verifyToken(String token){
        JWTVerifier verify = JWT
                .require(Algorithm.HMAC256(secretKey))
                .build();
        DecodedJWT verifyToken = verify.verify(token);
       return verifyToken.getClaim("email").asString();

    }

    public ProfileUser getProfile()throws NotFoundException{
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));
        String fullName = user.getUserInfo()!=null&& user.getUserInfo().getFullName()!=null ? user.getUserInfo().getFullName() : "No fullName";
        String biography=user.getUserInfo()!=null&&user.getUserInfo().getBiography() !=null ? user.getUserInfo().getBiography() : "No biography";
        String image=user.getUserInfo()!=null&&user.getUserInfo().getImage()!=null ? user.getUserInfo().getImage() : "No image";
        Gender gender=user.getUserInfo()!=null&& user.getUserInfo().getGender()!=null ? user.getUserInfo().getGender(): Gender.UNKNOWN;
        List<Long> subscribers = user.getFollower()!=null&& user.getFollower().getSubscribers()!=null ? user.getFollower().getSubscribers(): new ArrayList<>();
        List<Long> subscriptions =user.getFollower()!=null&& user.getFollower().getSubscriptions()!=null ? user.getFollower().getSubscriptions(): new ArrayList<>();
        List<Post> posts = user.getPosts()!=null? user.getPosts():new ArrayList<>();
        int countSubscribers = subscribers.size();
        int countSubscriptions = subscriptions.size();
        return ProfileUser
                .builder()
                .userName(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .fullName(fullName)
                .biography(biography)
                .image(image)
                .gender(gender)
                .subscribers(subscribers)
                .subscription(subscriptions)
                .posts(posts)
                .countSubscribers(countSubscribers)
                .countSubscriptions(countSubscriptions)

                .build();
    }
}
