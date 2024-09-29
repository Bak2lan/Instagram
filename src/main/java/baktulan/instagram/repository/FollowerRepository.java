package baktulan.instagram.repository;

import baktulan.instagram.dto.authenticationDTO.ProfileUser;
import baktulan.instagram.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowerRepository extends JpaRepository<Follower,Long> {

    @Query("select new baktulan.instagram.dto.authenticationDTO.ProfileUser(u.username,u.email,u.phoneNumber,uu,uf,size(uf.subscribers),size(uf.subscriptions) ) from User  u left join u.userInfo uu left join u.follower uf where u.username ilike concat(:name,'%') or uu.fullName ilike concat(:name,'%')")
    List<ProfileUser> search(String name);
}
