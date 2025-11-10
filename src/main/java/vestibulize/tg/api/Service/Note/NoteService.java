package vestibulize.tg.api.Service.Note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Entity.Note;
import vestibulize.tg.api.Repository.Note.NoteRepository;
import java.util.List;
import java.lang.Exception;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note createNote(Note note) {
        return noteRepository.save(note);
    }

    public List<Note> listNotes(Long notebook_id, String search) {

        if (search == null || search.trim().isEmpty()) {
            return noteRepository.listByNotebookId(notebook_id);
        }
        
        return noteRepository.listByNotebookIdAndFilter(notebook_id, search.trim());
    }

    public Note getNote(Long id) {
        return noteRepository.findById(id).orElse(new Note());
    }

    public Note updateNote(Note note, Long id) {
        
        Note searchedNote = noteRepository.findById(id).orElse(new Note());
 
        if (searchedNote.getId() == null) {
            throw new RuntimeException("Note not found");
        }
        
        searchedNote.setTitle(note.getTitle());
        searchedNote.setContent(note.getContent());
        searchedNote.setQuestions(note.getQuestions());
        searchedNote.setSummary(note.getSummary());
        
        return noteRepository.save(searchedNote);

    }

    public String deleteNote(Long id, Long notebook_id) {

        Note searchedNote = noteRepository.findById(id).orElse(new Note());

        if (searchedNote.getId() == null) {
            throw new RuntimeException("Note not found");
        }

        noteRepository.delete(searchedNote);
        
        return "Note deleted successfully";
    }
}