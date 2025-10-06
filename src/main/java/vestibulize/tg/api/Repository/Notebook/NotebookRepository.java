package vestibulize.tg.api.Repository.Notebook;

import vestibulize.tg.api.Entity.Notebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotebookRepository extends JpaRepository<Notebook, Long>{

}