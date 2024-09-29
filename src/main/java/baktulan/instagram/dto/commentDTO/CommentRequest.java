package baktulan.instagram.dto.commentDTO;

import baktulan.instagram.dto.likeDTO.LikeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentRequest {
    private String comment;
    private LocalDate createdAt;


}
