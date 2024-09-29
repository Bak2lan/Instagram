package baktulan.instagram.dto.postDTO;

import baktulan.instagram.dto.commentDTO.CommentResponse;
import baktulan.instagram.dto.imageDTO.ImageResponse;
import baktulan.instagram.dto.likeDTO.LikeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@NoArgsConstructor
@Data
@Builder

public class PostResponse {
    private String title;
    private String description;
    private List<ImageResponse>imageResponses;
    private LocalDate createdAt;
    private List<CommentResponse>comments;
    private int countOfLikes;

    public PostResponse(String title, String description, List<ImageResponse> imageResponses, LocalDate createdAt) {
        this.title = title;
        this.description = description;
        this.imageResponses = imageResponses;
        this.createdAt = createdAt;
    }

    public PostResponse(String title, String description, List<ImageResponse> imageResponses, LocalDate createdAt,List<CommentResponse>comments ,int countOfLikes) {
        this.title = title;
        this.description = description;
        this.imageResponses = imageResponses;
        this.createdAt = createdAt;
        this.comments=comments;
        this.countOfLikes = countOfLikes;
    }


}


