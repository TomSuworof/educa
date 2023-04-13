package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.Exercise;
import com.dreamteam.eduuca.entities.ExerciseState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, UUID> {
    Optional<Exercise> findByTitle(String title);

    Optional<Exercise> findByCustomUrl(String customUrl);

    Page<Exercise> findExercisesByState(ExerciseState state, Pageable pageable);

    @Query(value = "select * from t_exercise where to_tsvector(title || ' ' || custom_url || ' ' || content || ' ' || solution)  @@ to_tsquery( :query );", nativeQuery = true)
    List<Exercise> fullTextSearch(String query);

    Page<Exercise> findExercisesByStateAndTags_IdIn(ExerciseState state, Set<UUID> tags_id, Pageable pageable);
}
