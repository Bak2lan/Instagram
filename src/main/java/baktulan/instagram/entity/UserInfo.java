package baktulan.instagram.entity;

import baktulan.instagram.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_infos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfo {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_info_gen"
    )
    @SequenceGenerator(
            name = "user_info_gen",
            sequenceName = "user_info_seq",
            allocationSize = 1
    )
    private Long id;
    private String fullName;
    private String biography;
    private Gender gender;
    private String image;
}