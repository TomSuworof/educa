package com.dreamteam.eduuca.payload.response.article;

import com.dreamteam.eduuca.entities.article.Article;
import com.dreamteam.eduuca.entities.article.theme.Theme;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Optional;
import java.util.UUID;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class ArticleFullDTO extends ArticleShortDTO {
    protected final String content;
    protected final UUID themeID;

    public ArticleFullDTO(Article article) {
        super(article);
        this.content = article.getContent();
        this.themeID = Optional.ofNullable(article.getTheme()).map(Theme::getId).orElse(null);
    }
}
