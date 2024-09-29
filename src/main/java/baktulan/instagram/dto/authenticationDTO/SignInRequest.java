package baktulan.instagram.dto.authenticationDTO;

import baktulan.instagram.validation.email.EmailValidation;
import baktulan.instagram.validation.password.PasswordValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SignInRequest {
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;
}
