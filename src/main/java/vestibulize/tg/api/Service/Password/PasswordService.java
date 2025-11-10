package vestibulize.tg.api.Service.Password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Entity.User;
import vestibulize.tg.api.Repository.User.UserRepository;
import vestibulize.tg.api.Repository.PasswordRequest.PasswordRequestRepository;
import vestibulize.tg.api.Entity.PasswordRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import vestibulize.tg.api.Utils.Email.EmailSendGrid;
import java.io.IOException;
import java.time.LocalDateTime;


@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordRequestRepository passwordRequestRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void requestResetPassword(String email) {

        try {

            User user = userRepository.findByEmail(email);
            
            if (user == null) {
                throw new RuntimeException("Algo deu de errado. Tente novamente.");
            }

            PasswordRequest passwordRequest = new PasswordRequest(null, user.getId());
            
            passwordRequestRepository.save(passwordRequest);
            
            EmailSendGrid emailSendGrid = new EmailSendGrid(email, "Redefinição de Senha");
            emailSendGrid.sendChangePasswordRequest(passwordRequest.getToken());
                
        } catch (IOException e) {
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }

    }

}
