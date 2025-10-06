package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import vestibulize.tg.api.Entity.Notebook;
import vestibulize.tg.api.Service.Notebook.NotebookService;

@CrossOrigin(origins = "*")
@RestController
public class NotebookController {
    @Autowired
    private NotebookService notebookService;

    @PostMapping("/notebook")
    public ResponseEntity<Notebook> createNotebook(@Valid @RequestBody Notebook notebook) {

        try {
            Notebook createdNotebook = notebookService.createNotebook(notebook);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotebook);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Notebook());
        }
        
    }

    @GetMapping("/notebook")
    public ResponseEntity<List<Notebook>> listNotebooks() {
        return ResponseEntity.status(HttpStatus.OK).body(notebookService.listNotebooks());
    }

    @PutMapping("/notebook/{id}")
    public ResponseEntity<Notebook> updateNotebook(@PathVariable Long id, @Valid @RequestBody Notebook notebook) {
        return ResponseEntity.status(HttpStatus.OK).body(notebookService.updateNotebook(notebook, id));
    }

    @DeleteMapping("/notebook/{id}")
    public ResponseEntity<String> deleteNotebook(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(notebookService.deleteNotebook(id));
    }

}