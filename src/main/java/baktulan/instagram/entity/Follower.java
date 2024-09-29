package baktulan.instagram.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "followers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Follower {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "follower_gen"
    )
    @SequenceGenerator(
            name = "follower_gen",
            sequenceName = "follower_seq",
            allocationSize = 1
    )
    private Long id;
    @ElementCollection
    @JsonIgnore
    private List<Long>subscribers;
    @ElementCollection
    @JsonIgnore
    private List<Long>subscriptions;
}
