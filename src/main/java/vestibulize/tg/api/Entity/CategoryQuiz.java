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
public class CategoryQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false, insertable = false, updatable = false)
    private Quiz quiz;
    
    private Long quiz_id;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false, insertable = false, updatable = false)
    private Category category;
    
    private Long category_id;
    
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public CategoryQuiz() {}

    public CategoryQuiz(Long id, Long quiz_id, Long category_id) {
        this.setId(id);
        this.setQuiz_id(quiz_id);
        this.setCategory_id(category_id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(Long quiz_id) {
        this.quiz_id = quiz_id;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
