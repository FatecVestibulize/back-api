package vestibulize.tg.api.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.persistence.CascadeType;
import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.util.List;

@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String type;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;
    
    private Long user_id;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<CategoryQuiz> categoryQuizzes;
    
    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<AnswerQuizQuestion> answerQuizQuestions;
    
    @Transient
    private List<Question> questions;
    
    @Transient
    private List<Long> categories_ids;
    
    @Transient
    private List<Category> categories;
    
    private LocalDateTime finished_at;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

    public Quiz() {}

    public Quiz(Long id, Long user_id, String type) {
        this.setId(id);
        this.setUser_id(user_id);
        this.setType(type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CategoryQuiz> getCategoryQuizzes() {
        return categoryQuizzes;
    }

    public void setCategoryQuizzes(List<CategoryQuiz> categoryQuizzes) {
        this.categoryQuizzes = categoryQuizzes;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<AnswerQuizQuestion> getAnswerQuizQuestions() {
        return answerQuizQuestions;
    }

    public void setAnswerQuizQuestions(List<AnswerQuizQuestion> answerQuizQuestions) {
        this.answerQuizQuestions = answerQuizQuestions;
    }

    public LocalDateTime getFinished_at() {
        return finished_at;
    }

    public void setFinished_at(LocalDateTime finished_at) {
        this.finished_at = finished_at;
    }
    public List<Long> getCategories_ids() {
        return categories_ids;
    }
    public void setCategories_ids(List<Long> categories_ids) {
        this.categories_ids = categories_ids;
    }

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
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

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
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
