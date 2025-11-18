package vestibulize.tg.api.Service.Question;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import vestibulize.tg.api.Entity.Question;
import vestibulize.tg.api.Repository.Question.QuestionRepository;
import vestibulize.tg.api.Repository.Answer.AnswerRepository;
import java.util.Arrays;
import vestibulize.tg.api.Entity.CategoryQuiz;
import vestibulize.tg.api.Repository.CategoryQuiz.CategoryQuizRepository;
import java.util.List;
import vestibulize.tg.api.Repository.Category.CategoryRepository;
import vestibulize.tg.api.Entity.Category;
@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private CategoryQuizRepository categoryQuizRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Question getRandomQuestion(Long quizId) {

        Question question = null;

        ArrayList<CategoryQuiz> category = categoryQuizRepository.listByQuizId(quizId);

        if(category.isEmpty()) {
            question = questionRepository.getRandomQuestion(quizId);
        }else{

            List<String> categoryIds = new ArrayList<>();
            for (CategoryQuiz c : category) {
                categoryIds.add(c.getCategory_id().toString());
            }

            question = questionRepository.getRandomQuestionByCategory(quizId, categoryIds);
        }

        if(question == null) {
            return new Question();
        }

        question.setCategory(categoryRepository.findById(question.getCategory_id()).orElse(new Category()));
        question.setAnswers(answerRepository.listByQuestionId(question.getId()));
        
        return question;

    }

}
