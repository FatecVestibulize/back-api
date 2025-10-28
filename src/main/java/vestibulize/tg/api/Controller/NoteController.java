package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import vestibulize.tg.api.Entity.Note;
import vestibulize.tg.api.Service.Note.NoteService;

import org.springframework.web.bind.annotation.RequestHeader;
import vestibulize.tg.api.Utils.JwtUtil;

@CrossOrigin(origins = "*")
@RestController
public class NoteController {
    @Autowired
    private NoteService noteService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/note")
    public ResponseEntity<Note> createNote(@RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Note note) {
        try {
            Note createdNote = noteService.createNote(note);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdNote);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Note());
        }
        
    }

    @GetMapping("/note/{id}")
    public ResponseEntity<Note> getNote(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(noteService.getNote(id));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Note());
        }
    }

    @PutMapping("/note/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Note note) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(note, id));
        } catch (Exception e) {
            System.out.println("=== ERROR IN UPDATE NOTEBOOK ===");
            System.out.println("Error type: " + e.getClass().getSimpleName());
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Note());
        }
        
    }

    @DeleteMapping("/note/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(noteService.deleteNote(id, jwtUtil.extractId(token)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during delete note: " + e.getMessage());
        }
    }

}