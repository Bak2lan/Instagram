package baktulan.instagram.repository;

import baktulan.instagram.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {

    @Query("select case when count (l) > 0 then true else false end  from Like l where l.user.id = :userId AND l.post.id = :postId")
    boolean existsLikeByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);

    Optional<Like>findLikeByUserIdAndPostId(Long userId,Long postId);

    @Query("select l from Like l where l.user.id=:userId and l.comment.id=:commentId")
     Optional<Like>findLikeByUserAndCommentId(Long userId,Long commentId);

   }
