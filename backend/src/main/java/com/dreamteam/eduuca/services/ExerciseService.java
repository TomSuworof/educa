package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.ExerciseState;
import com.dreamteam.eduuca.payload.response.ExerciseDTO;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.repositories.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    public void saveExercise(Exercise exercise) {
        exerciseRepository.save(exercise);

        switch (exercise.getState()) {
            case PUBLISHED -> savePublished(exercise);
            case IN_EDITING -> saveDraft(exercise);
            default -> throw new IllegalStateException();
        }
    }

    private void saveDraft(Exercise newExercise) {
        Optional<Exercise> oldExerciseOpt = exerciseRepository.findById(newExercise.getId());

        if (oldExerciseOpt.isEmpty()) {
            createDraft(newExercise);
        } else {
            editDraft(newExercise, oldExerciseOpt.get());
        }
    }

    private void createDraft(Exercise newExercise) {
        if (exerciseRepository.findByCustomUrl(newExercise.getCustomUrl()).isPresent()) {
            throw new IllegalStateException("Exercise with this URL already exists");
        }

        exerciseRepository.save(newExercise);
//        exerciseSearchRepository.save(newExercise);
    }

    private void editDraft(Exercise newExercise, Exercise oldExercise) {
        exerciseRepository.delete(oldExercise);
//        exerciseSearchRepository.delete(oldExercise);

        exerciseRepository.save(newExercise);
//        exerciseSearchRepository.save(newExercise);
    }

    private void savePublished(Exercise newExercise) {
        Optional<Exercise> oldExerciseOpt = exerciseRepository.findById(newExercise.getId());

        if (oldExerciseOpt.isEmpty()) {
            createPublished(newExercise);
        } else {
            editPublished(newExercise, oldExerciseOpt.get());
        }
    }

    private void createPublished(Exercise newExercise) {
        if (exerciseRepository.findByCustomUrl(newExercise.getCustomUrl()).isPresent()) {
            throw new IllegalStateException("Exercise with this URL already exists");
        }

        exerciseRepository.save(newExercise);
//        exerciseSearchRepository.save(newExercise);
    }

    private void editPublished(Exercise newExercise, Exercise oldExercise) {
        newExercise.setPublicationDate(oldExercise.getPublicationDate());

        exerciseRepository.delete(oldExercise);
//        exerciseSearchRepository.delete(oldExercise);

        exerciseRepository.save(newExercise);
//        exerciseSearchRepository.save(newExercise);
    }


    public void deleteExerciseById(UUID id) {
        if (exerciseRepository.findById(id).isPresent()) {
            exerciseRepository.deleteById(id);
        } else {
            throw new IllegalStateException();
        }
    }


    public Exercise getExerciseById(UUID id) {
        Optional<Exercise> exerciseOpt = exerciseRepository.findById(id);
        if (exerciseOpt.isPresent()) {
            return exerciseOpt.get();
        } else {
            throw new IllegalStateException();
        }
    }


    public PageResponseDTO<ExerciseDTO> getPageWithExercisesByState(ExerciseState state, Integer limit, Integer offset) {
        Page<Exercise> exercisePage;

        if (state.equals(ExerciseState.ALL)) {
            exercisePage = getAllExercisesPaginated(limit, offset);
        } else {
            exercisePage = getExercisesByStatePaginated(state, limit, offset);
        }

        // return response after filtering
        return new PageResponseDTO<>(
                offset > 0 && exercisePage.getTotalElements() > 0,
                (offset + limit) < exercisePage.getTotalElements(),
                exercisePage.getContent().stream().map(ExerciseDTO::new).toList(),
                exercisePage.getTotalElements());
    }

    private Page<Exercise> getExercisesByStatePaginated(ExerciseState state, Integer limit, Integer offset) {
        return exerciseRepository.findExercisesByState(state, getDefaultPageable(limit, offset));
    }

    private Page<Exercise> getAllExercisesPaginated(Integer limit, Integer offset) {
        return exerciseRepository.findAll(getDefaultPageable(limit, offset));
    }

    private Pageable getDefaultPageable(Integer limit, Integer offset) {
        return PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate"));
    }
}
