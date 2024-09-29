package baktulan.instagram.repository;

import baktulan.instagram.dto.userDTO.UserResponse;
import baktulan.instagram.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
    @Query("select count (u)>0 from User  u where u.username=:userName")
    boolean existsByUsername(String userName);

    @Query("select u from User u join u.userInfo uu where uu.id=:id")
    User getUserByUserInfoId(Long id);

    @Query("select new baktulan.instagram.dto.userDTO.UserResponse(u.username,u.email) from User  u ")
    Page<UserResponse>getAllUsers(Pageable pageable);




}
