package vestibulize.tg.api.Repository.Goal;

import vestibulize.tg.api.Entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long>{

    @Query(value = "SELECT * FROM goal WHERE user_id = ?1 ORDER BY id DESC", nativeQuery = true)
    List<Goal> listByUserId(Long user_id);

    @Query(value = "SELECT * FROM goal WHERE user_id = ?1 AND title LIKE '%?2%' ORDER BY id DESC", nativeQuery = true)
    List<Goal> listByUserIdAndFilter(Long user_id, String search);

}