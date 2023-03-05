package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.ExerciseState;
import com.dreamteam.eduuca.entities.User;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.repositories.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final UserService userService;
    private final ExerciseRepository exerciseRepository;

    public void saveExercise(@NotNull Exercise exercise) {
        log.debug("saveExercise() called. Exercise: {}", () -> exercise);

        switch (exercise.getState()) {
            case PUBLISHED -> savePublished(exercise);
            case IN_EDITING -> saveDraft(exercise);
            default -> {
                log.warn("saveExercise(). Exercise have incorrect state. Will throw exception");
                throw new IllegalStateException();
            }
        }
    }

    private void saveDraft(@NotNull Exercise newExercise) {
        log.debug("saveDraft() called. New exercise: {}", () -> newExercise);
        Optional<Exercise> oldExerciseOpt = exerciseRepository.findById(newExercise.getId());

        if (oldExerciseOpt.isEmpty()) {
            log.trace("saveDraft(). Old exercise with same ID={} does not exists. Going to create new", newExercise.getId());
            createDraft(newExercise);
        } else {
            log.trace("saveDraft(). Old exercise with same ID={} exists. Going to edit", newExercise.getId());
            editDraft(newExercise, oldExerciseOpt.get());
        }
    }

    private void createDraft(@NotNull Exercise newExercise) {
        log.debug("createDraft() called. New exercise: {}", () -> newExercise);
        if (exerciseRepository.findByCustomUrl(newExercise.getCustomUrl()).isPresent()) {
            log.warn("createDraft(). Exercise with same URL '{}' already exists. Will throw exception", newExercise.getCustomUrl());
            throw new IllegalStateException(String.format("Exercise with this URL %s already exists", newExercise.getCustomUrl()));
        }

        newExercise.setPublicationDate(OffsetDateTime.now());

        log.trace("createDraft(). Exercise to save: {}", () -> newExercise);
        exerciseRepository.save(newExercise);
        log.trace("createDraft(). Exercise successfully saved: {}", () -> newExercise);
    }

    private void editDraft(@NotNull Exercise newExercise, @NotNull Exercise oldExercise) {
        log.debug("editDraft() called. New exercise: {}, old exercise: {}", () -> newExercise, () -> oldExercise);

        if (newExercise.getAuthor().equals(oldExercise.getAuthor())) {
            throw new SecurityException();
        }

        newExercise.setPublicationDate(OffsetDateTime.now());

        exerciseRepository.delete(oldExercise);
        log.trace("editDraft(). Old exercise successfully deleted: {}", () -> oldExercise);

        exerciseRepository.save(newExercise);
        log.trace("editDraft(). Exercise successfully saved: {}", () -> newExercise);
    }

    private void savePublished(@NotNull Exercise newExercise) {
        log.debug("savePublished() called. New exercise: {}", () -> newExercise);

        Optional<Exercise> oldExerciseOpt = exerciseRepository.findById(newExercise.getId());

        if (oldExerciseOpt.isEmpty()) {
            log.trace("savePublished(). Old exercise with same ID={} does not exists, Going to create new", newExercise.getId());
            createPublished(newExercise);
        } else {
            log.trace("savePublished(). Old exercise with same ID={} exists. Going to edit", newExercise.getId());
            editPublished(newExercise, oldExerciseOpt.get());
        }
    }

    private void createPublished(@NotNull Exercise newExercise) {
        log.debug("createPublished() called. Ne exercise: {}", () -> newExercise);
        if (exerciseRepository.findByCustomUrl(newExercise.getCustomUrl()).isPresent()) {
            log.warn("createPublished(). Exercise with same URL '{}' already exists. Will throw exception", newExercise.getCustomUrl());
            throw new IllegalStateException(String.format("Exercise with this URL %s already exists", newExercise.getCustomUrl()));
        }

        newExercise.setPublicationDate(OffsetDateTime.now());

        log.trace("createPublished(). Exercise to save: {}", () -> newExercise);
        exerciseRepository.save(newExercise);
        log.trace("createPublished(). Exercise successfully saved: {}", () -> newExercise);
    }

    private void editPublished(@NotNull Exercise newExercise, @NotNull Exercise oldExercise) {
        log.debug("editPublished() called. New exercise: {}, old exercise: {}", () -> newExercise, () -> oldExercise);

        if (!newExercise.getAuthor().equals(oldExercise.getAuthor())) {
            throw new SecurityException();
        }

        newExercise.setPublicationDate(oldExercise.getPublicationDate());

        exerciseRepository.delete(oldExercise);
        log.trace("editPublished(). Old exercise successfully deleted: {}", () -> oldExercise);

        exerciseRepository.save(newExercise);
        log.trace("editPublished(). Exercise successfully saved: {}", () -> newExercise);
    }


    public void deleteExerciseById(@NotNull UUID id, @NotNull Authentication auth) {
        log.debug("deleteExerciseById() called. ID={}", id);
        Exercise exercise = getExerciseById(id, auth);
        log.trace("deleteExerciseById(). Exercise to delete: {}", () -> exercise);
        exerciseRepository.delete(exercise);
        log.trace("deleteExerciseById(). Exercise successfully deleted.");
    }


    public Exercise getExerciseById(@NotNull UUID id, @NotNull Authentication auth) {
        log.debug("getExerciseById() called. ID={}", id);
        User currentUser = userService.getUserFromAuthentication(auth);
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(id);
        if (exerciseOpt.isEmpty()) {
            log.warn("getExerciseById(). Exercise with ID={} does not exist. Will throw exception", id);
            throw new IllegalStateException();
        }
        Exercise exercise = exerciseOpt.get();

        if (!userService.canUserEditExercise(currentUser, exercise)) {
            log.warn("getExerciseById(). Current user does not have rights to access the exercise with ID={}. User: {}", () -> id, () -> currentUser);
            throw new SecurityException();
        }

        log.trace("getExerciseById(). Exercise with ID={} exists. Going to return: {}", () -> id, () -> exercise);
        return exerciseOpt.get();
    }


    public PageResponseDTO<ExerciseDTO> getPageWithExercisesByState(ExerciseState state, Integer limit, Integer offset) {
        log.debug("getPageWithExercisesByState() called. State: {}, limit: {}, offset: {}", state, limit, offset);

        Page<Exercise> exercisePage;

        if (state.equals(ExerciseState.ALL)) {
            exercisePage = getAllExercisesPaginated(limit, offset);
        } else {
            exercisePage = getExercisesByStatePaginated(state, limit, offset);
        }
        log.trace("getPageWithExercisesByState(). Got page: {}", () -> exercisePage);

        // return response after filtering
        return new PageResponseDTO<>(
                offset > 0 && exercisePage.getTotalElements() > 0,
                (offset + limit) < exercisePage.getTotalElements(),
                exercisePage.getTotalElements(),
                exercisePage.getContent().stream().map(ExerciseDTO::new).toList());
    }

    private Page<Exercise> getExercisesByStatePaginated(ExerciseState state, Integer limit, Integer offset) {
        log.debug("getExercisesByStatePaginated() called. State: {}, limit: {}, offset: {}", state, limit, offset);
        return exerciseRepository.findExercisesByState(state, getDefaultPageable(limit, offset));
    }

    private Page<Exercise> getAllExercisesPaginated(Integer limit, Integer offset) {
        log.debug("getAllExercisesPaginated() called. Limit: {}, offset: {}", limit, offset);
        return exerciseRepository.findAll(getDefaultPageable(limit, offset));
    }

    private Pageable getDefaultPageable(Integer limit, Integer offset) {
        log.debug("getDefaultPageable() called. Limit: {}, offset: {}", limit, offset);
        return PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate"));
    }
}
