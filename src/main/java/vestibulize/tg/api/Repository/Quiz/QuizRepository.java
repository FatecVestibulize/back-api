package vestibulize.tg.api.Repository.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import vestibulize.tg.api.Entity.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    @Query(value = "SELECT * FROM quiz WHERE user_id = ?1 ORDER BY id DESC", nativeQuery = true)
    List<Quiz> listByUserId(Long user_id);

    @Query(value = "SELECT * FROM quiz WHERE user_id = ?1 AND finished_at IS NOT NULL and type = 'exam' ORDER BY id DESC", nativeQuery = true)
    List<Quiz> listFinishedExams(Long user_id);

}
