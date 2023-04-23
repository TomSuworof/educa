package com.dreamteam.eduuca.entities.article;

import com.dreamteam.eduuca.entities.article.tag.Tag;
import com.dreamteam.eduuca.entities.article.theme.Theme;
import com.dreamteam.eduuca.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "t_article")
@NoArgsConstructor
public abstract class Article {
    @Id
    protected UUID id;

    @Column
    @Size(min = 1)
    @Field(type = FieldType.Text)
    protected String title;

    @Column
    protected String customUrl;

    @Column(columnDefinition = "text")
    @Field(type = FieldType.Text)
    protected String summary;

    @Size(min = 1)
    @Column(columnDefinition = "text")
    @Field(type = FieldType.Text)
    @ToString.Exclude
    protected String content;

    @PastOrPresent
    @Field(type = FieldType.Date)
    protected OffsetDateTime publicationDate;

    @Column
    protected ArticleState state;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    protected User author;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(name = "t_articles_tags",
            joinColumns = {@JoinColumn(name = "article_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    @org.springframework.data.annotation.Transient
    protected Set<Tag> tags;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "theme_id")
    @Nullable
    protected Theme theme;

    public abstract String dType();
}
