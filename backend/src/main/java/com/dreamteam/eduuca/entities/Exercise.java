package com.dreamteam.eduuca.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "t_exercise")
@NoArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column
    private String title;

    @Size(min = 1)
    @Column(columnDefinition = "text")
    @Field(type = FieldType.Text)
    private String content;

    @Size(min = 1)
    @Column(columnDefinition = "text")
    @Field(type = FieldType.Text)
    private String solution;

    @PastOrPresent
    @Field(type = FieldType.Date)
    private OffsetDateTime publicationDate;

    @Column
    private ExerciseState state;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.getId()) &&
                Objects.equals(title, exercise.getTitle()) &&
                Objects.equals(content, exercise.getContent()) &&
                Objects.equals(solution, exercise.getSolution()) &&
                Objects.equals(state, exercise.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, solution, state);
    }
}
