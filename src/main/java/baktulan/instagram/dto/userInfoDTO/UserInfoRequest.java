package baktulan.instagram.dto.userInfoDTO;

import baktulan.instagram.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfoRequest {

    private String fullName;
    private String biography;
    private Gender gender;
    private String image;

}
