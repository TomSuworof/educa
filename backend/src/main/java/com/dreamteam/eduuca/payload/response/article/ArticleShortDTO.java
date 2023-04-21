package com.dreamteam.eduuca.payload.response.article;

import com.dreamteam.eduuca.entities.Article;
import com.dreamteam.eduuca.entities.Tag;
import com.dreamteam.eduuca.payload.response.ObjectDTO;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public abstract class ArticleShortDTO implements ObjectDTO {
    protected final String dType;
    protected final UUID id;
    protected final String title;
    protected final String customUrl;
    protected final String summary;
    protected final String authorName;
    protected final List<String> tags;
    protected final OffsetDateTime publicationDate;
    protected final String state;

    public ArticleShortDTO(Article article) {
        this.dType = article.dType();
        this.id = article.getId();
        this.title = article.getTitle();
        this.customUrl = article.getCustomUrl();
        this.summary = article.getSummary();
        this.authorName = article.getAuthor().getUsername();
        this.tags = article.getTags().stream().map(Tag::getName).toList();
        this.publicationDate = article.getPublicationDate();
        this.state = article.getState().getDescription();
    }
}
