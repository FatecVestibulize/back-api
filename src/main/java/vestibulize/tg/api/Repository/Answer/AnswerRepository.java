package vestibulize.tg.api.Repository.Answer;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vestibulize.tg.api.Entity.Answer;
import org.springframework.data.jpa.repository.Query;
import java.util.ArrayList;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    @Query(value = "SELECT * FROM answer WHERE question_id = ?1 ORDER BY id ASC", nativeQuery = true)
    ArrayList<Answer> listByQuestionId(Long question_id);
}
