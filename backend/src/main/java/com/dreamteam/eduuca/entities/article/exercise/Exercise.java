package com.dreamteam.eduuca.entities.article.exercise;

import com.dreamteam.eduuca.entities.article.Article;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString(callSuper = true)
public class Exercise extends Article {
    @Size(min = 1)
    @Column(columnDefinition = "text")
    @Field(type = FieldType.Text)
    @ToString.Exclude
    private String solution;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.getId()) &&
                Objects.equals(title, exercise.getTitle()) &&
                Objects.equals(customUrl, exercise.getCustomUrl()) &&
                Objects.equals(content, exercise.getContent()) &&
                Objects.equals(solution, exercise.getSolution()) &&
                Objects.equals(state, exercise.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, customUrl, content, solution, state);
    }

    @Override
    public String dType() {
        return Exercise.class.getSimpleName();
    }
}
