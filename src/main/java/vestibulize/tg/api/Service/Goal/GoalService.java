package vestibulize.tg.api.Service.Goal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Entity.Goal;
import vestibulize.tg.api.Repository.Goal.GoalRepository;
import java.util.List;
import java.lang.Exception;

@Service
public class GoalService {

    @Autowired
    private GoalRepository goalRepository;

    public Goal createGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public List<Goal> listGoals(Long user_id, String search) {

        if (search == null || search.trim().isEmpty()) {
            return goalRepository.listByUserId(user_id);
        }
        
        return goalRepository.listByUserIdAndFilter(user_id, search.trim());
    }

    public Goal getGoal(Long id) {
        return goalRepository.findById(id).orElse(new Goal());
    }

    public Goal updateGoal(Goal goal, Long id) {
        
        Goal searchedGoal = goalRepository.findById(id).orElse(new Goal());
 
        if (searchedGoal.getId() == null) {
            throw new RuntimeException("Goal not found");
        }
        
        searchedGoal.setTitle(goal.getTitle());
        
        return goalRepository.save(searchedGoal);

    }

    public String deleteGoal(Long id, Long user_id) {

        Goal searchedGoal = goalRepository.findById(id).orElse(new Goal());

        if (searchedGoal.getId() == null) {
            throw new RuntimeException("Goal not found");
        }

        goalRepository.delete(searchedGoal);
        
        return "Goal deleted successfully";
    }
}