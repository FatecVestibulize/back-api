package vestibulize.tg.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import vestibulize.tg.api.Entity.Goal;
import vestibulize.tg.api.Service.Goal.GoalService;

import org.springframework.web.bind.annotation.RequestHeader;
import vestibulize.tg.api.Utils.JwtUtil;

@CrossOrigin(origins = "*")
@RestController
public class GoalController {
    @Autowired
    private GoalService goalService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/goal")
    public ResponseEntity<Goal> createGoal(@RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Goal goal) {
        try {
            goal.setUser_id(jwtUtil.extractId(token));
            Goal createdGoal = goalService.createGoal(goal);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Goal());
        }
        
    }

    @GetMapping("/goal")
    public ResponseEntity<List<Goal>> listGoals(@RequestHeader(value = "token", required = true) String token, @RequestParam(value = "search", required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(goalService.listGoals(jwtUtil.extractId(token), search));
    }

    @PutMapping("/goal/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token, @Valid @RequestBody Goal goal) {
        try{
            goal.setUser_id(jwtUtil.extractId(token));
            return ResponseEntity.status(HttpStatus.OK).body(goalService.updateGoal(goal, id));
        } catch (Exception e) {
            System.out.println("Error message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Goal());
        }
        
    }

    @DeleteMapping("/goal/{id}")
    public ResponseEntity<String> deleteGoal(@PathVariable Long id, @RequestHeader(value = "token", required = true) String token) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(goalService.deleteGoal(id, jwtUtil.extractId(token)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error during delete goal: " + e.getMessage());
        }
    }

}