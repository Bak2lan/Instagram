package baktulan.instagram.dto.userDTO;

import baktulan.instagram.enums.Role;
import baktulan.instagram.validation.email.EmailValidation;
import baktulan.instagram.validation.password.PasswordValidation;
import baktulan.instagram.validation.phoneNumber.PhoneNumberValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserRequest {
    private String userName;
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;
    @PhoneNumberValidation()
    private String phoneNumber;
    private Role role;
}
