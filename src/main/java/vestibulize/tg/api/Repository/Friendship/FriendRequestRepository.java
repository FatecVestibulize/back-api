package vestibulize.tg.api.Repository.Friendship;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vestibulize.tg.api.Entity.Friendship.FriendRequest;
import vestibulize.tg.api.Entity.User;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    List<FriendRequest> findByReceiver(User receiver);

    List<FriendRequest> findBySender(User sender);

    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
}
