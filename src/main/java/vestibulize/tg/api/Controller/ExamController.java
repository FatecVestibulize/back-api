package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import vestibulize.tg.api.Entity.Exam;
import vestibulize.tg.api.Service.Exam.ExamService;

import org.springframework.web.bind.annotation.RequestHeader;
import vestibulize.tg.api.Utils.JwtUtil;

@CrossOrigin(origins = "*")
@RestController
public class ExamController {
    @Autowired
    private ExamService examService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/exam")
    public ResponseEntity<Exam> createExam(@RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Exam exam) {
        try {
            exam.setUser_id(jwtUtil.extractId(token));
            Exam createdExam = examService.createExam(exam);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Exam());
        }
        
    }

    @GetMapping("/exam")
    public ResponseEntity<List<Exam>> listExams(@RequestHeader(value = "token", required = true) String token, @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(examService.listExams(jwtUtil.extractId(token), search));
    }

    @PutMapping("/exam/{id}")
    public ResponseEntity<Exam> updateExam(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Exam exam) {
        try{
            exam.setUser_id(jwtUtil.extractId(token));
            return ResponseEntity.status(HttpStatus.OK).body(examService.updateExam(exam, id));
        } catch (Exception e) {
            System.out.println("Error message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Exam());
        }
        
    }

    @DeleteMapping("/exam/{id}")
    public ResponseEntity<String> deleteExam(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(examService.deleteExam(id, jwtUtil.extractId(token)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during delete exam: " + e.getMessage());
        }
    }

}