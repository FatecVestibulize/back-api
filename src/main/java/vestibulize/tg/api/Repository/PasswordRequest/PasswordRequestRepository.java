package vestibulize.tg.api.Repository.PasswordRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vestibulize.tg.api.Entity.PasswordRequest;

@Repository
public interface PasswordRequestRepository extends JpaRepository<PasswordRequest, Long> {
    
    @Query(value = "SELECT * FROM password_request WHERE token = ?1 LIMIT 1", nativeQuery = true)
    PasswordRequest findByToken(String token);

}
