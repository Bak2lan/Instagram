package baktulan.instagram.dto.userDTO;

import baktulan.instagram.dto.postDTO.PostResponse;
import baktulan.instagram.entity.Post;
import baktulan.instagram.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResponse {


    private String userName;
    private String email;
    private String phoneNumber;


    private String fullName;
    private String biography;
    private String image;
    private Gender gender;
    private List<Long>subscribers;
    private List<Long>subscription;
    private List<PostResponse>posts;
    private boolean isSubscribed;
    private int countSubscribers;
    private int countSubscriptions;
    private String message;

    public UserResponse(String userName, String email, String phoneNumber, String fullName, String biography, Gender gender, String image, List<Long> subscribers, List<Long> subscription, List<PostResponse> posts) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.biography = biography;
        this.gender = gender;
        this.image=image;
        this.subscribers = subscribers;
        this.subscription = subscription;
        this.posts = posts;
    }

    public UserResponse(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }
}
