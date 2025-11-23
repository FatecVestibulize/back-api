package vestibulize.tg.api.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@Entity
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "notebook_id", nullable = false, insertable = false, updatable = false)
    private Notebook notebook;
    
    private Long notebook_id;
    private String title;
    private String content;
    private String questions;
    private String summary;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Note() {}

    public Note(Long id, Long notebook_id, String title, String content, String questions, String summary) {
        this.id = id;
        this.setNotebook_id(notebook_id);
        this.setTitle(title);
        this.setContent(content);
        this.setQuestions(questions);
        this.setSummary(summary);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Notebook getNotebook() {
        return notebook;
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
    }

    public Long getNotebook_id() {
        return notebook_id;
    }

    public void setNotebook_id(Long notebook_id) {
        this.notebook_id = notebook_id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getQuestions() {
        return questions;
    }

    public void setQuestions(String questions) {
        this.questions = questions;
    }
    
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }
}
