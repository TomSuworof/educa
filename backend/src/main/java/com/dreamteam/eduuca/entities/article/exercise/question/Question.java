package com.dreamteam.eduuca.entities.article.exercise.question;

import com.dreamteam.eduuca.entities.article.exercise.Exercise;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "t_question")
@NoArgsConstructor
public class Question {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Exercise exercise;

    @Column
    @NotEmpty
    private String answer;

    @Column
    private String remark;

    @Column
    private String hint;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return Objects.equals(id, question.id) && Objects.equals(exercise, question.exercise) && Objects.equals(answer, question.answer) && Objects.equals(remark, question.remark) && Objects.equals(hint, question.hint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exercise, answer, remark, hint);
    }
}
