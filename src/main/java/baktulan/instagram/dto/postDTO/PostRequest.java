package baktulan.instagram.dto.postDTO;

import baktulan.instagram.dto.commentDTO.CommentRequest;
import baktulan.instagram.dto.imageDTO.ImageRequest;
import baktulan.instagram.dto.likeDTO.LikeRequest;
import baktulan.instagram.entity.Image;
import baktulan.instagram.entity.Like;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
public class PostRequest {
    private String title ;
    private String description;
    private LocalDate createAt;
    private List<ImageRequest>images;
//    private List<LikeRequest>likes;
//    private List<CommentRequest>comments;

    public PostRequest(String title, String description, LocalDate createAt, List<ImageRequest> images) {
        this.title = title;
        this.description = description;
        this.createAt = createAt;
        this.images = images;
    }


}
