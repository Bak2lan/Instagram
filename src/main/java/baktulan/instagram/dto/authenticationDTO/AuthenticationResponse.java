package baktulan.instagram.dto.authenticationDTO;

import baktulan.instagram.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthenticationResponse {
    private String token;
    private String email;
    private Role role;

}
