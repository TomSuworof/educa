package com.dreamteam.eduuca.entities.article.tag;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "t_tag")
@NoArgsConstructor
public class Tag {
    @Id
    private UUID id;

    @Column
    @NotEmpty
    @Field(type = FieldType.Text)
    private String name;

    public Tag(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
}
