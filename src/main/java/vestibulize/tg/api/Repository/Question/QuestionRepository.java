package vestibulize.tg.api.Repository.Question;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.ArrayList;
import vestibulize.tg.api.Entity.Question;
import vestibulize.tg.api.Entity.CategoryQuiz;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    @Query(value = "SELECT question.* FROM question LEFT JOIN answer_quiz_question ON question_id = question.id and quiz_id = ?1  WHERE category_id IN (?2) AND deleted_at IS NULL AND question_id is null ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Question getRandomQuestionByCategory(Long quizId, List<String> categories);

    @Query(value = "SELECT question.* FROM question LEFT JOIN answer_quiz_question ON question_id = question.id and quiz_id = ?1 WHERE deleted_at IS NULL AND question_id is null ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Question getRandomQuestion(Long quizId);

}
