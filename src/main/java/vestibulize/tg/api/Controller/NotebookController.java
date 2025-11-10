package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import vestibulize.tg.api.Entity.Notebook;
import vestibulize.tg.api.Service.Notebook.NotebookService;

import org.springframework.web.bind.annotation.RequestHeader;
import vestibulize.tg.api.Utils.JwtUtil;

import vestibulize.tg.api.Entity.Note;
import vestibulize.tg.api.Service.Note.NoteService;

@CrossOrigin(origins = "*")
@RestController
public class NotebookController {
    @Autowired
    private NotebookService notebookService;
    
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/notebook")
    public ResponseEntity<Notebook> createNotebook(@RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Notebook notebook) {
        try {
            notebook.setUser_id(jwtUtil.extractId(token));
            Notebook createdNotebook = notebookService.createNotebook(notebook);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNotebook);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Notebook());
        }
        
    }

    @GetMapping("/notebook")
    public ResponseEntity<List<Notebook>> listNotebooks(@RequestHeader(value = "token", required = true) String token, @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(notebookService.listNotebooks(jwtUtil.extractId(token), search));
    }

    @PutMapping("/notebook/{id}")
    public ResponseEntity<Notebook> updateNotebook(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Notebook notebook) {
        try{
            notebook.setUser_id(jwtUtil.extractId(token));
            return ResponseEntity.status(HttpStatus.OK).body(notebookService.updateNotebook(notebook, id));
        } catch (Exception e) {
            System.out.println("=== ERROR IN UPDATE NOTEBOOK ===");
            System.out.println("Error type: " + e.getClass().getSimpleName());
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Notebook());
        }
        
    }

    @DeleteMapping("/notebook/{id}")
    public ResponseEntity<String> deleteNotebook(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(notebookService.deleteNotebook(id, jwtUtil.extractId(token)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during delete notebook: " + e.getMessage());
        }
    }


    @GetMapping("notebook/{notebook_id}/notes")
    public ResponseEntity<List<Note>> listNotes(@RequestHeader(value = "token", required = true) String token, @RequestParam(value = "search", required = false) String search, @PathVariable Long notebook_id) {
        return ResponseEntity.status(HttpStatus.OK).body(noteService.listNotes(notebook_id, search));
    }

}