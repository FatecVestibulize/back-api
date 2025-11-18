package vestibulize.tg.api.Repository.AnswerQuizQuestion;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vestibulize.tg.api.Entity.AnswerQuizQuestion;
import org.springframework.data.jpa.repository.Query;
import java.util.ArrayList;

@Repository
public interface AnswerQuizQuestionRepository extends JpaRepository<AnswerQuizQuestion, Long> {
    
    @Query(value = "SELECT * FROM answer_quiz_question WHERE quiz_id = ?1 ORDER BY id DESC", nativeQuery = true)
    ArrayList<AnswerQuizQuestion> listByQuizId(Long quiz_id);
}
