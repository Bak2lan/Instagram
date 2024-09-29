package baktulan.instagram.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Like {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "like_gen"
    )
    @SequenceGenerator(
            name = "like_gen",
            sequenceName = "like_seq",
            allocationSize = 1
    )
    private Long id;
    private boolean isLike;
    @OneToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    private User user;
    @ManyToOne
    private Post post;
    @ManyToOne
    private Comment comment;
}
