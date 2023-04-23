package com.dreamteam.eduuca.services.article.query;

import com.dreamteam.eduuca.entities.article.Article;
import com.dreamteam.eduuca.entities.article.ArticleState;
import com.dreamteam.eduuca.entities.article.tag.Tag;
import com.dreamteam.eduuca.entities.article.theme.Theme;
import com.dreamteam.eduuca.entities.user.User;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import com.dreamteam.eduuca.payload.response.article.ArticleShortDTO;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import com.dreamteam.eduuca.services.like.LikeService;
import com.dreamteam.eduuca.services.TagService;
import com.dreamteam.eduuca.services.ThemeService;
import com.dreamteam.eduuca.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractArticleQueryService<E extends Article, DTO extends ArticleShortDTO> {
    private final UserService userService;
    private final TagService tagService;
    private final ThemeService themeService;
    private final LikeService likeService;

    private final ArticleRepository<E> articleRepository;

    protected abstract @Nullable DTO parseToDTO(@NotNull E article, @Nullable Integer likeCount);

    private @Nullable DTO parseToDTO(@NotNull E article) {
        log.debug("parseToDTO() called.");
        return parseToDTO(article, likeService.getLikeCount(article.getId()));
    }

    public E getById(@NotNull UUID id, @NotNull Authentication auth) {
        log.debug("getArticleById() called. ID={}", id);
        User currentUser = userService.getUserFromAuthentication(auth);
        Optional<E> articleOpt = articleRepository.findById(id);
        if (articleOpt.isEmpty()) {
            log.warn("getArticleById(). Article with ID={} does not exist. Will throw exception", id);
            throw new EntityNotFoundException("Article with this ID does not exist");
        }
        E article = articleOpt.get();

        if (!userService.canUserEditArticle(currentUser, article)) {
            log.warn("getArticleById(). Current user does not have rights to access the article with ID={}. User: {}", () -> id, () -> currentUser);
            throw new SecurityException("User does not have rights to access the article");
        }

        log.trace("getArticleById(). Article with ID={} exists. Going to return: {}", () -> id, () -> article);
        return article;
    }

    public PageResponseDTO<DTO> getPageByState(ArticleState state, Integer limit, Integer offset) {
        log.debug("getPageWithArticlesByState() called. State: {}, limit: {}, offset: {}", state, limit, offset);

        Page<E> articlePage;

        if (state.equals(ArticleState.ALL)) {
            articlePage = getAllArticlesPaginated(limit, offset);
        } else {
            articlePage = getArticlesByStatePaginated(state, limit, offset);
        }
        log.trace("getPageWithArticlesByState(). Got page: {}", () -> articlePage);

        return new PageResponseDTO<>(
                offset > 0 && articlePage.getTotalElements() > 0,
                (offset + limit) < articlePage.getTotalElements(),
                articlePage.getTotalElements(),
                articlePage.getContent().stream().map(e -> (E) e).map(this::parseToDTO).filter(Objects::nonNull).toList());
    }

    private Page<E> getArticlesByStatePaginated(ArticleState state, Integer limit, Integer offset) {
        log.debug("getArticlesByStatePaginated() called. State: {}, limit: {}, offset: {}", state, limit, offset);
        return articleRepository.findByState(state, getDefaultPageable(limit, offset));
    }

    private Page<E> getAllArticlesPaginated(Integer limit, Integer offset) {
        log.debug("getAllArticlesPaginated() called. Limit: {}, offset: {}", limit, offset);
        return articleRepository.findAll(getDefaultPageable(limit, offset));
    }

    private Pageable getDefaultPageable(Integer limit, Integer offset) {
        log.debug("getDefaultPageable() called. Limit: {}, offset: {}", limit, offset);
        return PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate"));
    }

    public void deleteById(@NotNull UUID id, @NotNull Authentication auth) {
        log.debug("deleteById() called. ID={}", id);
        E article = getById(id, auth);
        log.trace("deleteById(). Article to delete: {}", () -> article);
        articleRepository.delete(article);
        log.trace("deleteById(). Article successfully deleted.");
    }

    public List<DTO> search(@NotNull String query) {
        log.debug("search() called. Query: {}", () -> query);

        return articleRepository
                .fullTextSearch(query)
                .stream()
                .peek(article -> log.trace("search(). Found article: {}", () -> article))
                .map(this::parseToDTO)
                .collect(Collectors.toList());
    }

    public PageResponseDTO<DTO> getPageByTags(List<String> tagNames, Integer limit, Integer offset) {
        log.debug("getPageByTags() called. Limit: {}, offset: {}", limit, offset);

        log.trace("getPageByTags(). Tag names count: {}", tagNames::size);
        Set<UUID> tagIDs = tagService.getTags(tagNames).stream().map(Tag::getId).collect(Collectors.toSet());
        log.trace("getPageByTags(). Tag IDs count: {}", tagIDs::size);

        Page<E> articles = articleRepository.findByStateAndTags_IdIn(ArticleState.PUBLISHED, tagIDs, PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate")));

        return new PageResponseDTO<>(
                offset > 0 && articles.getTotalElements() > 0,
                (offset + limit) < articles.getTotalElements(),
                articles.getTotalElements(),
                articles.getContent().stream().map(this::parseToDTO).toList()
        );
    }

    public PageResponseDTO<DTO> getPageByTheme(@NotNull UUID themeID, Integer limit, Integer offset) {
        log.debug("getPageByTheme() called. Theme ID: {}, limit: {}, offset: {}", themeID, limit, offset);

        Theme theme = themeService.getTheme(themeID);
        log.trace("getPageByTheme(). Found theme: {}", () -> theme);

        Page<E> articles = articleRepository.findByTheme(theme, PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "publicationDate")));

        return new PageResponseDTO<>(
                offset > 0 && articles.getTotalElements() > 0,
                (offset + limit) < articles.getTotalElements(),
                articles.getTotalElements(),
                articles.getContent().stream().map(this::parseToDTO).toList()
        );
    }
}
