package vestibulize.tg.api.Service.Notebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Entity.Notebook;
import vestibulize.tg.api.Repository.Notebook.NotebookRepository;
import java.util.List;

@Service
public class NotebookService {

    @Autowired
    private NotebookRepository notebookRepository;

    public Notebook createNotebook(Notebook notebook) {
        return notebookRepository.save(notebook);
    }

    public List<Notebook> listNotebooks() {
        return notebookRepository.findAll();
    }

    public Notebook updateNotebook(Notebook notebook, Long id) {
        notebook.setId(id);
        return notebookRepository.save(notebook);
    }

    public String deleteNotebook(Long id) {
        try {
            notebookRepository.deleteById(id);
            return "Notebook deleted successfully";
        } catch (Exception e) {
            return "Error during delete notebook: " + e.getMessage();
        }
    }
}