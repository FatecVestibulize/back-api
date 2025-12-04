package vestibulize.tg.api.Service.Quiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vestibulize.tg.api.Repository.Quiz.QuizRepository;
import vestibulize.tg.api.Entity.Quiz;

import java.text.DecimalFormat;
import java.util.List;
import vestibulize.tg.api.Entity.AnswerQuizQuestion;
import vestibulize.tg.api.Repository.AnswerQuizQuestion.AnswerQuizQuestionRepository;
import vestibulize.tg.api.Repository.Answer.AnswerRepository;
import vestibulize.tg.api.Entity.Answer;
import vestibulize.tg.api.Repository.Question.QuestionRepository;
import vestibulize.tg.api.Entity.Question;
import vestibulize.tg.api.Entity.CategoryQuiz;
import vestibulize.tg.api.Repository.CategoryQuiz.CategoryQuizRepository;
import vestibulize.tg.api.Repository.Category.CategoryRepository;
import vestibulize.tg.api.Entity.Category;

@Service
public class QuizService {
    
    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerQuizQuestionRepository answerQuizQuestionRepository;

    @Autowired
    private CategoryQuizRepository categoryQuizRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Quiz createQuiz(Quiz quiz) {

        Quiz savedQuiz = quizRepository.save(quiz);

        if (quiz.getCategories_ids() != null && !quiz.getCategories_ids().isEmpty()) {

            for(Long category_id : quiz.getCategories_ids()) {
                CategoryQuiz categoryQuiz = new CategoryQuiz();

                categoryQuiz.setCategory_id(category_id);
                categoryQuiz.setQuiz_id(savedQuiz.getId());

                categoryQuizRepository.save(categoryQuiz);

            }
        }

        return savedQuiz;
    }

    public List<Quiz> listQuizzes(Long user_id) {
        return quizRepository.listByUserId(user_id);
    }

    public AnswerQuizQuestion answerQuestion(AnswerQuizQuestion answerQuizQuestion) {

        Answer answer = answerRepository.findById(answerQuizQuestion.getAnswer_id()).orElse(new Answer());

        if(answer.getQuestion_id() != answerQuizQuestion.getQuestion_id()) {
            throw new RuntimeException("Answer is from another question");
        }

        return answerQuizQuestionRepository.save(answerQuizQuestion);

    }

    public Quiz getQuiz(Long id, Long user_id) {

        Quiz quiz = quizRepository.findById(id).orElse(new Quiz());

        if (quiz.getUser_id() == null || !quiz.getUser_id().equals(user_id)) {
            throw new RuntimeException("Quiz not found");
        }

        return quiz;
    }

    public Quiz patchQuiz(Long id, Long user_id, Quiz quiz) {
        
        Quiz searchedQuiz = quizRepository.findById(id).orElse(new Quiz());

        if (searchedQuiz.getUser_id() == null || !searchedQuiz.getUser_id().equals(user_id)) {
            throw new RuntimeException("Quiz not found");
        }

        searchedQuiz.setFinished_at(quiz.getFinished_at());

        return quizRepository.save(searchedQuiz);
    }

    public List<AnswerQuizQuestion> reviewQuiz(Long id, Long user_id) {
        
        Quiz quiz = quizRepository.findById(id).orElse(new Quiz());

        if (quiz.getUser_id() == null || !quiz.getUser_id().equals(user_id)) {
            throw new RuntimeException("Quiz not found");
        }

        List<AnswerQuizQuestion> answerQuizQuestions = answerQuizQuestionRepository.listByQuizId(id);
        for (AnswerQuizQuestion answerQuizQuestion : answerQuizQuestions) {
            
            answerQuizQuestion.setQuestion(questionRepository.findById(answerQuizQuestion.getQuestion_id()).orElse(new Question()));
            answerQuizQuestion.getQuestion().setAnswers(answerRepository.listByQuestionId(answerQuizQuestion.getQuestion_id()));
            answerQuizQuestion.getQuestion().setCategory(categoryRepository.findById(answerQuizQuestion.getQuestion().getCategory_id()).orElse(new Category()));
            answerQuizQuestion.setAnswer(answerRepository.findById(answerQuizQuestion.getAnswer_id()).orElse(new Answer()));
            answerQuizQuestion.setQuiz(quizRepository.findById(id).orElse(new Quiz()));

        }

        return answerQuizQuestions;
    }

    public String calculateScore(Long user_id) {
        
        List<Quiz> quiz = quizRepository.listFinishedExams(user_id);
        
        List<Quiz> primeirosRegistros;

        if (quiz.size() >= 10) {
            primeirosRegistros = quiz.subList(0, 10);
        } else if (quiz.size() == 0) {
            return "0.0";
        }{ 
            primeirosRegistros = quiz.subList(0, quiz.size());
        }
        Double totalCorrectAnswers = 0.0;
        for (Quiz q : primeirosRegistros) {
            List<AnswerQuizQuestion> answerQuizQuestions = answerQuizQuestionRepository.listByQuizId(q.getId());
            for (AnswerQuizQuestion answerQuizQuestion : answerQuizQuestions) {
                answerQuizQuestion.setAnswer(answerRepository.findById(answerQuizQuestion.getAnswer_id()).orElse(new Answer()));

                if(answerQuizQuestion.getAnswer().getIs_correct()) {
                    totalCorrectAnswers++;
                }    
            }
        }
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format((Double) (totalCorrectAnswers / (primeirosRegistros.size() * 10) * 100));
        
    }    

}
