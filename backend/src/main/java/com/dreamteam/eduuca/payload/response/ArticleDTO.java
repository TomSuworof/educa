package com.dreamteam.eduuca.payload.response;

import com.dreamteam.eduuca.entities.Article;
import com.dreamteam.eduuca.entities.Tag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ArticleDTO implements ObjectDTO {
    protected final String dType;
    protected final UUID id;
    protected final String title;
    protected final String authorName;
    protected final String content;
    protected final List<String> tags;
    protected final OffsetDateTime publicationDate;
    protected final String state;

    public ArticleDTO(Article article) {
        this.dType = article.dType();
        this.id = article.getId();
        this.title = article.getTitle();
        this.authorName = article.getAuthor().getUsername();
        this.content = article.getContent();
        this.tags = article.getTags().stream().map(Tag::getName).toList();
        this.publicationDate = article.getPublicationDate();
        this.state = article.getState().getDescription();
    }
}
