package vestibulize.tg.api.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;

import jakarta.persistence.Transient;

import java.util.Random;
import java.nio.charset.StandardCharsets;

@Entity
public class PasswordRequest {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Long user_id;
    @Transient
    private User user;
    private LocalDateTime expiration_at;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private LocalDateTime deleted_at;

    public PasswordRequest() {}

    public PasswordRequest(Long id, Long user_id) {
        this.setId(id);
        this.setToken(token);
        this.setUser_id(user_id);
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getUser_id() {
        return user_id;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
    
    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    
    public void setToken(String token) {

        int length = 32;
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        this.token = sb.toString();

    }

    public String getToken() {
        return token;
    }

    public void setExpiration_at(LocalDateTime expiration_at) {
        this.expiration_at = expiration_at;
    }

    public LocalDateTime getExpiration_at() {
        return expiration_at;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }
    
    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }
    
    public void setDeleted_at(LocalDateTime deleted_at) {
        this.deleted_at = deleted_at;
    }

    public LocalDateTime getDeleted_at() {
        return deleted_at;
    }

    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
        updated_at = LocalDateTime.now();
        expiration_at = LocalDateTime.now().plusMinutes(15);
    }

    @PreUpdate
    protected void onUpdate() {
        updated_at = LocalDateTime.now();
    }
    
}