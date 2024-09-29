package baktulan.instagram.securityConfig;

import baktulan.instagram.entity.User;
import baktulan.instagram.repository.UserRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization!=null&&authorization.startsWith("Bearer ")){
            String token=authorization.substring(7);
            try {
                if (StringUtils.hasText(token)) {
                    String email = jwtService.verifyToken(token);
                    User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("Not found email"));
                    SecurityContextHolder.getContext().setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    user.getEmail(),
                                    null,
                                    user.getAuthorities()
                            )
                    );
                }
            }catch (JWTVerificationException e){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Invalid");
            }
        }
        filterChain.doFilter(request,response);


    }
}
