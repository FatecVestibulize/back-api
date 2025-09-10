package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import vestibulize.tg.api.Entity.User;
import vestibulize.tg.api.Service.User.UserService;

@CrossOrigin(origins = "*")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

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
        try{
            User loggedUser = userService.authenticate(user);
            return ResponseEntity.status(HttpStatus.OK).body(loggedUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new User());
        }
    }

}