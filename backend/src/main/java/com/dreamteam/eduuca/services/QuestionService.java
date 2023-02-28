package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.Question;
import com.dreamteam.eduuca.payload.request.AnswerRequest;
import com.dreamteam.eduuca.payload.request.QuestionUploadRequest;
import com.dreamteam.eduuca.payload.response.AnswerResponse;
import com.dreamteam.eduuca.payload.response.QuestionDTO;
import com.dreamteam.eduuca.repositories.ExerciseRepository;
import com.dreamteam.eduuca.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final ExerciseRepository exerciseRepository;
    private final QuestionRepository questionRepository;

    public QuestionDTO addQuestion(QuestionUploadRequest questionUploadRequest) {
        log.debug("addQuestion() called. Request: {}", () -> questionUploadRequest);
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(questionUploadRequest.getExerciseId());
        if (exerciseOpt.isEmpty()) {
            log.warn("addQuestion(). Exercise with required ID={} not found", questionUploadRequest.getExerciseId());
            throw new IllegalArgumentException("Exercise does not exist");
        }
        Exercise exercise = exerciseOpt.get();

        Question question = new Question();
        question.setId(UUID.randomUUID());
        question.setExercise(exercise);
        question.setAnswer(questionUploadRequest.getAnswer());
        question.setRemark(questionUploadRequest.getRemark());
        question.setHint(questionUploadRequest.getHint());

        Question questionSaved = questionRepository.save(question);
        log.trace("addQuestion(). Saved question: {}", () -> questionSaved);

        return new QuestionDTO(
                questionSaved.getId(),
                questionSaved.getExercise().getId(),
                questionSaved.getAnswer(),
                questionSaved.getRemark(),
                questionSaved.getHint()
        );
    }

    public void deleteQuestion(UUID questionId) {
        log.debug("deleteQuestion() called. Question ID: {}", questionId);

        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            log.warn("deleteQuestion(). Question with ID={} not found", questionId);
            throw new IllegalArgumentException("Question does not exist");
        }
        Question question = questionOpt.get();
        questionRepository.delete(question);
        log.trace("deleteQuestion(). Question deleted successfully");
    }

    public AnswerResponse answerQuestion(AnswerRequest answerRequest) {
        log.debug("answerQuestion() called. Request: {}", () -> answerRequest);

        Optional<Question> questionOpt = questionRepository.findById(answerRequest.getQuestionId());
        if (questionOpt.isEmpty()) {
            log.warn("answerQuestion(). Question with ID={} not found", answerRequest.getQuestionId());
            throw new IllegalArgumentException("Question does not exist");
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
