package baktulan.instagram.dto.commentDTO;

import baktulan.instagram.dto.likeDTO.LikeResponse;
import baktulan.instagram.dto.userDTO.UserResponse;
import baktulan.instagram.dto.userDTO.UserResponseForComment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@NoArgsConstructor
@Data
@Builder
public class CommentResponse {
    private String comment;
    private LocalDate createdAt;

    private UserResponseForComment username;
    private int countOfLikes;

    public CommentResponse(String comment, LocalDate createdAt, UserResponseForComment username, int countOfLikes) {
        this.comment = comment;
        this.createdAt = createdAt;
        this.username = username;
        this.countOfLikes = countOfLikes;
    }
}
