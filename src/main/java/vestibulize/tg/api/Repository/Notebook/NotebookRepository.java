package vestibulize.tg.api.Repository.Notebook;

import vestibulize.tg.api.Entity.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Long>{

    @Query(value = "SELECT * FROM notebook WHERE user_id = ?1 ORDER BY id DESC", nativeQuery = true)
    List<Notebook> listByUserId(Long user_id);

    @Query(value = "SELECT * FROM notebook WHERE user_id = ?1 AND title LIKE %?2% ORDER BY id DESC", nativeQuery = true)
    List<Notebook> listByUserIdAndFilter(Long user_id, String search);

}