package vestibulize.tg.api.Repository.SupportMaterial;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vestibulize.tg.api.Entity.SupportMaterial;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface SupportMaterialRepository extends JpaRepository<SupportMaterial, Long> {

    @Query(value = "SELECT * FROM support_material WHERE note_id = ?1 ORDER BY id DESC", nativeQuery = true)
    List<SupportMaterial> listByNoteId(Long note_id);

}
