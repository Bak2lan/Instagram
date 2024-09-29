package baktulan.instagram.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class    Image {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "image_gen"
    )
    @SequenceGenerator(
            name = "image_gen",
            sequenceName = "image_seq",
            allocationSize = 1
    )
    private Long id;
    private String imageLink;
    @ManyToOne(cascade = CascadeType.ALL)
    private Post post;

    public Image(String imageLink) {
    }
}

