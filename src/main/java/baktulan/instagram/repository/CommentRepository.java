package baktulan.instagram.repository;

import baktulan.instagram.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    @Query("select count (l)>0 from Like l where l.user.id=:userId and l.comment.id=:commentId ")
    boolean existLikeToComment(Long userId, Long commentId);
}
