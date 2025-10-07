package vestibulize.tg.api.Service.Notebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Entity.Notebook;
import vestibulize.tg.api.Repository.Notebook.NotebookRepository;
import java.util.List;
import java.lang.Exception;

@Service
public class NotebookService {

    @Autowired
    private NotebookRepository notebookRepository;

    public Notebook createNotebook(Notebook notebook) {
        return notebookRepository.save(notebook);
    }

    public List<Notebook> listNotebooks(Long user_id) {
        return notebookRepository.listByUserId(user_id);
    }

    public Notebook updateNotebook(Notebook notebook, Long id) {
        
        Notebook searchedNotebook = notebookRepository.findById(id).orElse(new Notebook());
 
        if (!searchedNotebook.getUser_id().equals(notebook.getUser_id()) || searchedNotebook.getId() == null) {
            throw new RuntimeException("Notebook not found");
        }
        
        searchedNotebook.setTitle(notebook.getTitle());
        searchedNotebook.setDescription(notebook.getDescription());
        
        return notebookRepository.save(searchedNotebook);

    }

    public String deleteNotebook(Long id, Long user_id) {

        Notebook searchedNotebook = notebookRepository.findById(id).orElse(new Notebook());

        if (searchedNotebook.getUser_id() != user_id || searchedNotebook.getId() == null) {
            throw new RuntimeException("Notebook not found");

        }

        notebookRepository.delete(searchedNotebook);
        
        return "Notebook deleted successfully";
    }
}