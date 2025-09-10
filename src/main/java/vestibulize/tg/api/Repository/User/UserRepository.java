package vestibulize.tg.api.Repository.User;

import vestibulize.tg.api.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    @Query(value = "INSERT INTO user (username, email, password, birth_date) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    Boolean create(User user);

    @Query(value = "SELECT * FROM user WHERE username = ?1 LIMIT 1", nativeQuery = true)
    User findByUsername(String username);

}