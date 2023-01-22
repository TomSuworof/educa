package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.ExerciseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    Optional<Exercise> findByTitle(String title);

    Optional<Exercise> findByCustomUrl(String customUrl);

    Page<Exercise> findExercisesByState(ExerciseState state, Pageable pageable);
}
