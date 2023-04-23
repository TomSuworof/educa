package com.dreamteam.eduuca.payload.response.article.exercise;

import com.dreamteam.eduuca.entities.article.exercise.Exercise;
import com.dreamteam.eduuca.payload.response.article.ArticleFullDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ExerciseFullDTO extends ArticleFullDTO {
    protected final String solution;

    public ExerciseFullDTO(Exercise exercise) {
        super(exercise);
        this.solution = exercise.getSolution();
    }
}
