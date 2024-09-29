package baktulan.instagram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "comments_gen"
    )
    @SequenceGenerator(
            name = "comments_gen",
            sequenceName = "comments_seq",
            allocationSize = 1
    )
    private Long id;
    private String comment;
    private LocalDate createdAt;
    @ManyToOne(cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JsonIgnore
    private User user;
    @ManyToOne
    private Post post;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "comment")
    private List<Like>likes;


}
