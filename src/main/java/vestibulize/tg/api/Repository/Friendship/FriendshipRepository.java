package vestibulize.tg.api.Repository.Friendship;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vestibulize.tg.api.Entity.Friendship.Friendship;
import vestibulize.tg.api.Entity.User;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findByUserAOrUserB(User userA, User userB);

    Optional<Friendship> findByUserAAndUserB(User userA, User userB);

    Optional<Friendship> findByUserBAndUserA(User userB, User userA);
}
