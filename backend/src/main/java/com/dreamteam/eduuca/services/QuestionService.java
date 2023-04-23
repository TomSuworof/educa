package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.article.exercise.Exercise;
import com.dreamteam.eduuca.entities.article.exercise.question.Question;
import com.dreamteam.eduuca.entities.user.User;
import com.dreamteam.eduuca.payload.request.AnswerRequest;
import com.dreamteam.eduuca.payload.response.AnswerResponse;
import com.dreamteam.eduuca.payload.common.QuestionDTO;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import com.dreamteam.eduuca.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final UserService userService;

    private final ArticleRepository<Exercise> exerciseRepository;
    private final QuestionRepository questionRepository;

    public @NotNull QuestionDTO getQuestion(@NotNull UUID questionId) {
        log.debug("getQuestion() called. Question ID={}", questionId);
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            log.warn("getQuestion(). Question with ID={} not found", questionId);
            throw new EntityNotFoundException("Question does not exist");
        }
        Question question = questionOpt.get();
        log.trace("getQuestion(). Question found: {}", () -> question);
        return new QuestionDTO(question.getId(), question.getExercise().getId(), null, question.getRemark(), question.getHint());
    }

    public @NotNull QuestionDTO saveQuestion(@NotNull QuestionDTO questionUploadRequest, @NotNull Authentication auth) {
        log.debug("saveQuestion() called. Request: {}", () -> questionUploadRequest);
        Question question = new Question();

        if (questionUploadRequest.id() != null) {
            log.trace("saveQuestion(). Request contains ID, going to set");
            question.setId(questionUploadRequest.id());
        } else {
            log.trace("saveQuestion(). Request does not contain ID, setting new random");
            question.setId(UUID.randomUUID());
        }

        Optional<Exercise> exerciseOpt = exerciseRepository.findById(questionUploadRequest.exerciseId());
        if (exerciseOpt.isEmpty()) {
            log.warn("saveQuestion(). Exercise with required ID={} not found", questionUploadRequest.exerciseId());
            throw new EntityNotFoundException("Exercise does not exist");
        }
        Exercise exercise = exerciseOpt.get();

        User currentUser = userService.getUserFromAuthentication(auth);
        if (!userService.canUserEditArticle(currentUser, exercise)) {
            log.warn("saveQuestion(). Current user does not have rights to access exercise and questions. Exercise ID={}, user: {}", exercise::getId, () -> currentUser);
            throw new SecurityException("Current user does not have rights to access exercise and questions");
        }

        question.setExercise(exercise);
        question.setAnswer(questionUploadRequest.answer());
        question.setRemark(questionUploadRequest.remark());
        question.setHint(questionUploadRequest.hint());

        log.trace("saveQuestion(). Saving question: {}", () -> question);
        Question questionSaved = questionRepository.save(question);
        log.trace("saveQuestion(). Saved question: {}", () -> questionSaved);

        return new QuestionDTO(
                questionSaved.getId(),
                questionSaved.getExercise().getId(),
                questionSaved.getAnswer(),
                questionSaved.getRemark(),
                questionSaved.getHint()
        );
    }

    public void deleteQuestion(@NotNull UUID questionId, @NotNull Authentication auth) {
        log.debug("deleteQuestion() called. Question ID: {}", questionId);

        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            log.warn("deleteQuestion(). Question with ID={} not found", questionId);
            throw new EntityNotFoundException("Question does not exist");
        }
        Question question = questionOpt.get();

        User currentUser = userService.getUserFromAuthentication(auth);
        if (!userService.canUserEditArticle(currentUser, question.getExercise())) {
            log.warn("deleteQuestion(). Current user cannot access question. Question ID={}, user: {}", () -> questionId, () -> currentUser);
        }

        questionRepository.delete(question);
        log.trace("deleteQuestion(). Question deleted successfully");
    }

    public AnswerResponse answerQuestion(@NotNull AnswerRequest answerRequest) {
        log.debug("answerQuestion() called. Request: {}", () -> answerRequest);

        Optional<Question> questionOpt = questionRepository.findById(answerRequest.getQuestionId());
        if (questionOpt.isEmpty()) {
            log.warn("answerQuestion(). Question with ID={} not found", answerRequest.getQuestionId());
            throw new EntityNotFoundException("Question does not exist");
        }
        Question question = questionOpt.get();

        String verdict = validateAnswer(question, answerRequest);

        AnswerResponse response = new AnswerResponse();
        response.setQuestionId(answerRequest.getQuestionId());
        response.setAnswer(answerRequest.getAnswer());
        response.setVerdict(verdict);
        return response;
    }

    private String validateAnswer(Question question, AnswerRequest answer) {
        // todo add answer validation

        if (question.getAnswer().equals(answer.getAnswer())) {
            return "OK";
        } else {
            return "WRONG";
        }
    }
}
