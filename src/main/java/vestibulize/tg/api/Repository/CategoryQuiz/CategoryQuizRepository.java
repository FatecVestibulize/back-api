package vestibulize.tg.api.Repository.CategoryQuiz;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vestibulize.tg.api.Entity.CategoryQuiz;
import org.springframework.data.jpa.repository.Query;
import java.util.ArrayList;

@Repository
public interface CategoryQuizRepository extends JpaRepository<CategoryQuiz, Long> {
    
    @Query(value = "SELECT * FROM category_quiz WHERE quiz_id = ?1 ORDER BY id DESC", nativeQuery = true)
    ArrayList<CategoryQuiz> listByQuizId(Long quiz_id);
}
