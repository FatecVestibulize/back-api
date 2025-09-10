package vestibulize.tg.api.Service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Entity.User;
import vestibulize.tg.api.Repository.User.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {     
        return userRepository.save(user);
    }
}