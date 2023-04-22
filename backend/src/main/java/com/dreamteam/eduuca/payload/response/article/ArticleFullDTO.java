package com.dreamteam.eduuca.payload.response.article;

import com.dreamteam.eduuca.entities.Article;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public abstract class ArticleFullDTO extends ArticleShortDTO {
    protected final String content;

    public ArticleFullDTO(Article article) {
        super(article);
        this.content = article.getContent();
    }
}
