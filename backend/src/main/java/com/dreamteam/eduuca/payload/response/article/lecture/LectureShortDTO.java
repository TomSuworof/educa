package com.dreamteam.eduuca.payload.response.article.lecture;


import com.dreamteam.eduuca.entities.article.lecture.Lecture;
import com.dreamteam.eduuca.payload.response.article.ArticleShortDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LectureShortDTO extends ArticleShortDTO {
    public LectureShortDTO(Lecture lecture, Integer likeCount) {
        super(lecture, likeCount);
    }
}
