package com.dreamteam.eduuca.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor
public class Lecture extends Article {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(id, lecture.getId()) &&
                Objects.equals(title, lecture.getTitle()) &&
                Objects.equals(customUrl, lecture.getCustomUrl()) &&
                Objects.equals(content, lecture.getContent()) &&
                Objects.equals(state, lecture.getState());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, customUrl, content, state);
    }

    @Override
    public String dType() {
        return Lecture.class.getSimpleName();
    }
}
