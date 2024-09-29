package baktulan.instagram.dto.authenticationDTO;

import baktulan.instagram.entity.Follower;
import baktulan.instagram.entity.Post;
import baktulan.instagram.entity.UserInfo;
import baktulan.instagram.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProfileUser {
    private String userName;
    private String email;
    private String phoneNumber;


    private String fullName;
    private String biography;
    private String image;
    private Gender gender;
    private List<Long>subscribers;
    private List<Long>subscription;
    private List<Post>posts;
    private boolean isSubscribed;
    private int countSubscribers;
    private int countSubscriptions;
    private String message;

    public ProfileUser(String userName, String email, String phoneNumber, String fullName, String biography, String image) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = fullName;
        this.biography = biography;
        this.image = image;
    }

    public ProfileUser(String userName, String email, String phoneNumber, UserInfo userInfo, Follower follower,int countSubscribers,int countSubscriptions) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.fullName = userInfo!=null&&userInfo.getFullName()!=null?userInfo.getFullName():null;
        this.biography = userInfo!=null&&userInfo.getBiography()!=null?userInfo.getBiography():null;
        this.image = userInfo!=null&&userInfo.getImage()!=null?userInfo.getImage():null;
        this.gender = userInfo!=null&&userInfo.getGender()!=null?userInfo.getGender():Gender.UNKNOWN;
        this.subscribers = follower!=null&&follower.getSubscribers()!=null?follower.getSubscribers():new ArrayList<>();
        this.subscription = follower!=null&&follower.getSubscriptions()!=null?follower.getSubscriptions():new ArrayList<>();
        this.countSubscribers=follower!=null&&follower.getSubscribers()!=null?follower.getSubscribers().size():0;
        this.countSubscriptions=follower!=null&&follower.getSubscriptions()!=null?follower.getSubscriptions().size():0;
    }
}
