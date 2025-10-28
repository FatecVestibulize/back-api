package vestibulize.tg.api.Repository.Note;

import vestibulize.tg.api.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long>{

    @Query(value = "SELECT * FROM note WHERE notebook_id = ?1 ORDER BY id DESC", nativeQuery = true)
    List<Note> listByNotebookId(Long notebook_id);

    @Query(value = "SELECT * FROM note WHERE notebook_id = ?1 AND title LIKE '%?2%' ORDER BY id DESC", nativeQuery = true)
    List<Note> listByNotebookIdAndFilter(Long notebook_id, String search);

}