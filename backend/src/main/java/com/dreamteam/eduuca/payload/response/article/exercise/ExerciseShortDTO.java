package com.dreamteam.eduuca.payload.response.article.exercise;

import com.dreamteam.eduuca.entities.article.exercise.Exercise;
import com.dreamteam.eduuca.payload.response.article.ArticleShortDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExerciseShortDTO extends ArticleShortDTO {
    public ExerciseShortDTO(Exercise exercise) {
        super(exercise);
    }
}
