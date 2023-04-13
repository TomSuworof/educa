package com.dreamteam.eduuca.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "t_exercise")
@NoArgsConstructor
public class Exercise {
    @Id
    private UUID id;

    @Column
    @Size(min = 1)
    @Field(type = FieldType.Text)
    private String title;

    @Column
    private String customUrl;

    @Size(min = 1)
    @Column(columnDefinition = "text")
    @Field(type = FieldType.Text)
    @ToString.Exclude
    private String content;

    @Size(min = 1)
    @Column(columnDefinition = "text")
    @Field(type = FieldType.Text)
    @ToString.Exclude
    private String solution;

    @PastOrPresent
    @Field(type = FieldType.Date)
    private OffsetDateTime publicationDate;

    @Column
    private ExerciseState state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "t_exercises_tags",
            joinColumns = {@JoinColumn(name = "exercise_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @org.springframework.data.annotation.Transient
    private Set<Tag> tags;

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
}
