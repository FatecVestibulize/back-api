package vestibulize.tg.api.Service.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import vestibulize.tg.api.Entity.User;
import vestibulize.tg.api.Entity.Friendship.Friendship;
import vestibulize.tg.api.Entity.Friendship.FriendRequest;
import vestibulize.tg.api.Entity.Friendship.Friendship;
import vestibulize.tg.api.Entity.User;
import vestibulize.tg.api.Repository.Friendship.FriendRequestRepository;
import vestibulize.tg.api.Repository.Friendship.FriendshipRepository;
import vestibulize.tg.api.Repository.User.UserRepository;
import vestibulize.tg.api.Service.Exam.ExamService;
import vestibulize.tg.api.Service.Goal.GoalService;
import vestibulize.tg.api.Service.Notebook.NotebookService;
import vestibulize.tg.api.Utils.JwtUtil;
import vestibulize.tg.api.Service.Storage.S3StorageService;

import org.springframework.web.multipart.MultipartFile;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private NotebookService notebookService;
    @Autowired
    private GoalService goalService;
    @Autowired
    private ExamService examService;

    @Autowired
    private S3StorageService s3StorageService;

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User authenticate(User userRequest) {
        User userOptional = userRepository.findByEmail(userRequest.getEmail());
        if (userOptional == null || !passwordEncoder.matches(userRequest.getPassword(), userOptional.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(userOptional.getEmail(), userOptional.getId());
        processLogin(userOptional.getId());
        return new User(token, userOptional.getUsername(), userOptional.getEmail(), userOptional.getAvatar_url(), userOptional.getLoginStreak());
    }

    public User updateUser(String token, User updatedUser, MultipartFile avatar) {
        Long userId = jwtUtil.extractId(token);
        User existingUser = userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()) {
            existingUser.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        //novo
        if (updatedUser.getAvatarColor() != null && !updatedUser.getAvatarColor().isBlank()) {
            existingUser.setAvatarColor(updatedUser.getAvatarColor());
        } 

        //novo 2
        if (updatedUser.getInterest() != null && !updatedUser.getInterest().isBlank()) {
            existingUser.setInterest(updatedUser.getInterest());
        } 

        userRepository.save(existingUser);
        String newToken = jwtUtil.generateToken(existingUser.getUsername(), existingUser.getId());
        return new User(newToken, existingUser.getUsername(), existingUser.getEmail(), existingUser.getAvatar_url(), existingUser.getLoginStreak());
    }

    // Contagem e resumo
    public int countCadernos(Long userId) {
        return notebookService.listNotebooks(userId, null).size();
    }

    public int countMetas(Long userId) {
        return goalService.listGoals(userId, null).size();
    }

    public LocalDate getNextExamDate(Long userId) {
        LocalDate today = LocalDate.now();
        return examService.listExams(userId, null).stream()
                .map(e -> e.getDate().toLocalDate())
                .filter(d -> !d.isBefore(today))
                .sorted()
                .findFirst()
                .orElse(null);
    }

    // Friendships
    public void sendFriendRequest(Long userId, Long targetId) {
        if (Objects.equals(userId, targetId)) return;

        User sender = userRepository.findById(Math.toIntExact(userId)).orElseThrow();
        User receiver = userRepository.findById(Math.toIntExact(targetId)).orElseThrow();

        boolean exists = friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent()
                || friendshipRepository.findByUserAOrUserB(sender, sender)
                .stream().anyMatch(f -> f.getUserA().equals(receiver) || f.getUserB().equals(receiver));

        if (!exists) {
            friendRequestRepository.save(new FriendRequest(null, sender, receiver));
        }
    }

    public void acceptFriendRequest(Long userId, Long fromId) {
        User receiver = userRepository.findById(Math.toIntExact(userId)).orElseThrow();
        User sender = userRepository.findById(Math.toIntExact(fromId)).orElseThrow();

        friendRequestRepository.findBySenderAndReceiver(sender, receiver)
                .ifPresent(req -> {
                    friendRequestRepository.delete(req);
                    friendshipRepository.save(new Friendship(null, sender, receiver));
                    friendshipRepository.save(new Friendship(null, receiver, sender));
                });
    }

    public void rejectFriendRequest(Long userId, Long fromId) {
        User receiver = userRepository.findById(Math.toIntExact(userId)).orElseThrow();
        User sender = userRepository.findById(Math.toIntExact(fromId)).orElseThrow();
        friendRequestRepository.findBySenderAndReceiver(sender, receiver)
                .ifPresent(friendRequestRepository::delete);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow();
        User friend = userRepository.findById(Math.toIntExact(friendId)).orElseThrow();

        friendshipRepository.findByUserAOrUserB(user, user).stream()
                .filter(f -> f.getUserA().equals(friend) || f.getUserB().equals(friend))
                .forEach(friendshipRepository::delete);
    }

    public List<User> listFriends(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow();
        List<Friendship> friendships = friendshipRepository.findByUserAOrUserB(user, user);
        return friendships.stream()
                .map(f -> f.getUserA().equals(user) ? f.getUserB() : f.getUserA())
                .toList();
    }

    public List<User> listFriendRequests(Long userId) {
        User receiver = userRepository.findById(Math.toIntExact(userId)).orElseThrow();
        List<FriendRequest> requests = friendRequestRepository.findByReceiver(receiver);
        return requests.stream().map(FriendRequest::getSender).toList();
    }

    public List<User> listAllUsers(Long userId) {
        return userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(userId))
                .collect(Collectors.toList());
    }

    public boolean hasPendingRequest(Long userId, Long targetId) {
        User sender = userRepository.findById(Math.toIntExact(userId)).orElseThrow();
        User receiver = userRepository.findById(Math.toIntExact(targetId)).orElseThrow();
        return friendRequestRepository.findBySenderAndReceiver(sender, receiver).isPresent();
    }

    //online status
    public void setUserOnline(String username, boolean online) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.setOnline(online);
            userRepository.save(user);
        }
    }

    //offline
    public void setUserOffline(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow();
        user.setOnline(false);
        userRepository.save(user);
    }
    public List<Long> getOnlineUsers() {
        return userRepository.findAll().stream()
                .filter(User::isOnline)
                .map(User::getId)
                .toList();
    }
    //novo
    public User getLoggedUser(String token) {
        Long userId = jwtUtil.extractId(token);
        return userRepository.findById(Math.toIntExact(userId))
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public User uploadAvatar(Long userId, MultipartFile file) {
        try {
            User user = userRepository.findById(Math.toIntExact(userId))
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            user.setAvatar_url(s3StorageService.uploadFile(file, "users/" + userId + "/avatar"));
            userRepository.save(user);
            
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload do avatar: " + e.getMessage());
        }

    }    
    // Atualiza streak do usuário ao fazer login
    public User processLogin(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId))
            .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();
        LocalDate lastLogin = user.getLastLoginDate();

        if (lastLogin == null) {
        // Primeiro login ou nunca registrado → inicia sequência
        user.setLoginStreak(1);
        } else {
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(lastLogin, today);

            if (daysBetween == 0) {
            // Login no mesmo dia → sequência se mantém
                } else if (daysBetween == 1) {
            // Login consecutivo → +1
                user.setLoginStreak(user.getLoginStreak() + 1);
                } else {
            // Perdeu 2+ dias → reinicia
                user.setLoginStreak(1);
            }
        }

        user.setLastLoginDate(today);
        return userRepository.save(user);
    }

}

