package com.dreamteam.eduuca.repositories;

import com.dreamteam.eduuca.entities.article.lecture.Lecture;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends ArticleRepository<Lecture> {
}
