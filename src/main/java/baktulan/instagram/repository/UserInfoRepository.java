package baktulan.instagram.repository;

import baktulan.instagram.dto.userDTO.UserResponse;
import baktulan.instagram.dto.userInfoDTO.UserInfoResponse;
import baktulan.instagram.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Long> {

    @Query("select new baktulan.instagram.dto.userInfoDTO.UserInfoResponse(uu.fullName,uu.biography,uu.gender,uu.image) from User u  join u.userInfo uu where u.id=:userId")
    UserInfoResponse getByUserId(Long userId);



}
