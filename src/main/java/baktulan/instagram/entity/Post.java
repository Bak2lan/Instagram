package baktulan.instagram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.lang.invoke.CallSite;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "post_gen"
    )
    @SequenceGenerator(
            name = "post_gen",
            sequenceName = "post_seq",
            allocationSize = 1
    )
    private Long id;
    private String title;
    private String description;
    private LocalDate createdAt;
    @ManyToOne(cascade ={
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
    })
    @JsonIgnore
    private User user;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "post")
    private List<Comment>comments;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Image>images;
    @OneToMany(cascade = CascadeType.PERSIST,mappedBy = "post")
    @JsonIgnore
    private List<Like>likes;
}
