package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.Lecture;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends ArticleRepository<Lecture> {
}
