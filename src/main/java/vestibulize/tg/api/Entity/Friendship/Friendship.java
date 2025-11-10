package vestibulize.tg.api.Entity.Friendship;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import vestibulize.tg.api.Entity.User;

@Entity
@Table(name = "friendships")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_a_id")
    private User userA;

    @ManyToOne
    @JoinColumn(name = "user_b_id")
    private User userB;

    public Friendship() {
    }

    public Friendship(Long id, User userA, User userB) {
        this.id = id;
        this.userA = userA;
        this.userB = userB;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserA() {
        return userA;
    }

    public void setUserA(User userA) {
        this.userA = userA;
    }

    public User getUserB() {
        return userB;
    }

    public void setUserB(User userB) {
        this.userB = userB;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id=" + id +
                ", userA=" + (userA != null ? userA.getUsername() : "null") +
                ", userB=" + (userB != null ? userB.getUsername() : "null") +
                '}';
    }
}
