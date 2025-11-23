package vestibulize.tg.api.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Notebook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    private String title;
    private String description;
    
    @OneToMany(mappedBy = "notebook", cascade = CascadeType.ALL)
    private List<Note> notes;
    
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public Notebook() {}

    public Notebook(String title, String description) {
        this.setTitle(title);
        this.setDescription(description);
    }

    public Notebook(Long id, String title, String description, LocalDateTime created_at, LocalDateTime updated_at) {
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setCreated_at(created_at);
        this.setUpdated_at(updated_at);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
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