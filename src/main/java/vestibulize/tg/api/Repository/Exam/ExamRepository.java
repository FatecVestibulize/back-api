package vestibulize.tg.api.Repository.Exam;

import vestibulize.tg.api.Entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long>{

    @Query(value = "SELECT * FROM exam WHERE user_id = ?1 ORDER BY id DESC", nativeQuery = true)
    List<Exam> listByUserId(Long user_id);

    @Query(value = "SELECT * FROM exam WHERE user_id = ?1 AND name LIKE '%?2%' ORDER BY id DESC", nativeQuery = true)
    List<Exam> listByUserIdAndFilter(Long user_id, String search);

}