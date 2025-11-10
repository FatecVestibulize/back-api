package vestibulize.tg.api.Service.Exam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Entity.Exam;
import vestibulize.tg.api.Repository.Exam.ExamRepository;
import java.util.List;
import java.lang.Exception;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    public Exam createExam(Exam exam) {
        return examRepository.save(exam);
    }

    public List<Exam> listExams(Long user_id, String search) {

        if (search == null || search.trim().isEmpty()) {
            return examRepository.listByUserId(user_id);
        }
        
        return examRepository.listByUserIdAndFilter(user_id, search.trim());
    }

    public Exam getExam(Long id) {
        return examRepository.findById(id).orElse(new Exam());
    }

    public Exam updateExam(Exam exam, Long id) {
        
        Exam searchedExam = examRepository.findById(id).orElse(new Exam());
 
        if (searchedExam.getId() == null) {
            throw new RuntimeException("Exam not found");
        }
        
        searchedExam.setDate(exam.getDate());
        searchedExam.setName(exam.getName());
        
        return examRepository.save(searchedExam);

    }

    public String deleteExam(Long id, Long user_id) {

        Exam searchedExam = examRepository.findById(id).orElse(new Exam());

        if (searchedExam.getId() == null) {
            throw new RuntimeException("Exam not found");
        }

        examRepository.delete(searchedExam);
        
        return "Exam deleted successfully";
    }
}