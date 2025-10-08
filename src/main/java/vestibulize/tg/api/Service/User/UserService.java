package vestibulize.tg.api.Service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Entity.User;
import vestibulize.tg.api.Repository.User.UserRepository;
import vestibulize.tg.api.Utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(User userRequest) {

        User userOptional = userRepository.findByUsername(userRequest.getUsername());

        if (!passwordEncoder.matches(userRequest.getPassword(), userOptional.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(userOptional.getUsername(), userOptional.getId());

        return (new User(token, userOptional.getUsername(), userOptional.getEmail()));
    }


}