package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import vestibulize.tg.api.Entity.Quiz;
import vestibulize.tg.api.Service.Quiz.QuizService;
import vestibulize.tg.api.Utils.JwtUtil;
import java.util.List;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.web.bind.annotation.GetMapping;
import vestibulize.tg.api.Entity.AnswerQuizQuestion;
import vestibulize.tg.api.Entity.Question;
import vestibulize.tg.api.Service.Question.QuestionService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@CrossOrigin(origins = "*")
public class QuizController {
    
    @Autowired
    private QuizService quizService;

    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/quiz")
    public ResponseEntity<Quiz> createQuiz(@RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Quiz quiz) {
        
        try {

            Long user_id = jwtUtil.extractId(token);
            quiz.setUser_id(user_id);
            Quiz createdQuiz = quizService.createQuiz(quiz);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Quiz());
        }

    }

    @GetMapping("/quiz")
    public ResponseEntity<List<Quiz>> listQuizzes(@RequestHeader(value = "token", required = true) String token) {
        
        try {
            Long user_id = jwtUtil.extractId(token);
            List<Quiz> quizzes = quizService.listQuizzes(user_id);
            return ResponseEntity.status(HttpStatus.OK).body(quizzes);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<Quiz>());
        }
    }

    @GetMapping("/quiz/{id}")
    public ResponseEntity<Quiz> getQuiz(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token) {
        try {
            Long user_id = jwtUtil.extractId(token);
            Quiz quiz = quizService.getQuiz(id, user_id);
            return ResponseEntity.status(HttpStatus.OK).body(quiz);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Quiz());
        }
    }

    @PatchMapping("/quiz/{id}")
    public ResponseEntity<Quiz> patchQuiz(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Quiz quiz) {
        try {
            Long user_id = jwtUtil.extractId(token);
            Quiz patchedQuiz = quizService.patchQuiz(id, user_id, quiz);
            return ResponseEntity.status(HttpStatus.OK).body(patchedQuiz);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Quiz());
        }
    }

    @PostMapping("/quiz/answer-question")
    public ResponseEntity<String> answerQuestion(@RequestHeader(value = "token", required = true) String token, @Valid @RequestBody AnswerQuizQuestion answerQuizQuestion) {
        try {
            quizService.answerQuestion(answerQuizQuestion);
            return ResponseEntity.status(HttpStatus.CREATED).body("Question answered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during answer question: " + e.getMessage());
        }
    }

    @GetMapping("/quiz/{id}/question")
    public ResponseEntity<Question> createQuestion(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.getRandomQuestion(id));
    }

    @GetMapping("/quiz/{id}/review")
    public ResponseEntity<List<AnswerQuizQuestion>> reviewQuiz(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token) {
        try {
            Long user_id = jwtUtil.extractId(token);
            List<AnswerQuizQuestion> answerQuizQuestions = quizService.reviewQuiz(id, user_id);
            return ResponseEntity.status(HttpStatus.OK).body(answerQuizQuestions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ArrayList<AnswerQuizQuestion>());
        }
    }

}
