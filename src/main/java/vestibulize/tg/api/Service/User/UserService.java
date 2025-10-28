package vestibulize.tg.api.Service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Entity.User;
import vestibulize.tg.api.Repository.User.UserRepository;
import vestibulize.tg.api.Service.Notebook.NotebookService;
import vestibulize.tg.api.Utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotebookService notebookService;

    //armazenamento simples de amigos (mock)
    private final java.util.Map<Long, List<Long>> friendMap = new java.util.HashMap<>(); // NOVO

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
        return new User(token, userOptional.getUsername(), userOptional.getEmail());
    }

    public User updateUser(String token, User updatedUser) {
        Long userId = jwtUtil.extractId(token);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()) {
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepository.save(existingUser);

        String newToken = jwtUtil.generateToken(existingUser.getUsername(), existingUser.getId());

        return new User(newToken, existingUser.getUsername(), existingUser.getEmail());
    }

    public int countCadernos(Long userId) {
        return notebookService.listNotebooks(userId, null).size();
    }

}