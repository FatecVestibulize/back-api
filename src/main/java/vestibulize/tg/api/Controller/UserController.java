package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import vestibulize.tg.api.Entity.User;
import vestibulize.tg.api.Service.User.UserService;
import vestibulize.tg.api.Utils.JwtUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/user/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new User());
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<User> loginUser(@Valid @RequestBody User user) {
        try {
            User loggedUser = userService.authenticate(user);
            return ResponseEntity.status(HttpStatus.OK).body(loggedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new User());
        }
    }

    @PutMapping("/user/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authHeader,
                                        @RequestBody User updatedUser) {
        try {
            String token = authHeader.replace("Bearer ", "");
            User user = userService.updateUser(token, updatedUser);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/resumo")
    public ResponseEntity<?> getResumo(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtUtil.extractId(token);

            int totalCadernos = userService.countCadernos(userId);
            int totalMetas = userService.countMetas(userId);
            LocalDate proximaData = userService.getNextExamDate(userId);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            var resumo = new java.util.HashMap<String, Object>();
            resumo.put("cadernos", totalCadernos);
            resumo.put("quizzes", 6);
            resumo.put("metasAtivas", totalMetas);
            resumo.put("proximaData", proximaData != null ? proximaData.format(formatter) : "Nenhum exame");

            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping("/user/friends")
    public ResponseEntity<?> getFriends(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtUtil.extractId(token);
            List<User> friends = userService.listFriends(userId);
            return ResponseEntity.ok(friends);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/user/friend-request/{targetId}")
    public ResponseEntity<?> sendFriendRequest(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable Long targetId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtUtil.extractId(token);
            userService.sendFriendRequest(userId, targetId);
            return ResponseEntity.ok("Solicitação enviada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/user/friend-request/accept/{fromId}")
    public ResponseEntity<?> acceptFriendRequest(@RequestHeader("Authorization") String authHeader,
                                                 @PathVariable Long fromId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtUtil.extractId(token);
            userService.acceptFriendRequest(userId, fromId);
            return ResponseEntity.ok("Solicitação aceita");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/user/friend-request/reject/{fromId}")
    public ResponseEntity<?> rejectFriendRequest(@RequestHeader("Authorization") String authHeader,
                                                 @PathVariable Long fromId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtUtil.extractId(token);
            userService.rejectFriendRequest(userId, fromId);
            return ResponseEntity.ok("Solicitação recusada");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/user/friend-requests")
    public ResponseEntity<?> listFriendRequests(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtUtil.extractId(token);
            var requests = userService.listFriendRequests(userId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/user/friend/{friendId}")
    public ResponseEntity<?> removeFriend(@RequestHeader("Authorization") String authHeader,
                                          @PathVariable Long friendId) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtUtil.extractId(token);
            userService.removeFriend(userId, friendId);
            return ResponseEntity.ok("Amigo removido");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping("/user/all")
    public ResponseEntity<?> listAllUsers(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            Long userId = jwtUtil.extractId(token);
            var users = userService.listAllUsers(userId);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
