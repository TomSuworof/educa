package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.progress.Progress;
import com.dreamteam.eduuca.entities.progress.ProgressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, ProgressId> {
    List<Progress> findByUserID(UUID userID);

    List<Progress> findByArticleID(UUID articleID);
}
