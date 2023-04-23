package com.dreamteam.eduuca.payload.response.article.lecture;

import com.dreamteam.eduuca.entities.article.lecture.Lecture;
import com.dreamteam.eduuca.payload.response.article.ArticleFullDTO;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LectureFullDTO extends ArticleFullDTO {
    public LectureFullDTO(Lecture lecture) {
        super(lecture);
    }
}
