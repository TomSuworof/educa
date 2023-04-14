package com.dreamteam.eduuca.payload.response;


import com.dreamteam.eduuca.entities.Lecture;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class LectureDTO extends ArticleDTO {
    public LectureDTO(Lecture lecture) {
        super(lecture);
    }
}
