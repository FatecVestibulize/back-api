package vestibulize.tg.api.Repository.Category;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import vestibulize.tg.api.Entity.Category;
import org.springframework.data.jpa.repository.Query;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    @Query(value = "SELECT * FROM category WHERE deleted_at IS NULL ORDER BY id ASC", nativeQuery = true)
    ArrayList<Category> listAll();
}
