package com.dreamteam.eduuca.services.like;

import com.dreamteam.eduuca.entities.article.Article;
import com.dreamteam.eduuca.entities.article.ArticleState;
import com.dreamteam.eduuca.entities.article.like.Like;
import com.dreamteam.eduuca.entities.user.User;
import com.dreamteam.eduuca.payload.common.LikeDTO;
import com.dreamteam.eduuca.repositories.LikeRepository;
import com.dreamteam.eduuca.services.UserService;
import com.dreamteam.eduuca.services.article.query.ArticleQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class LikeUpdateService {
    private final UserService userService;
    private final ArticleQueryService articleQueryService;
    private final LikeRepository likeRepository;

    public void saveLike(@NotNull LikeDTO likeDTO, @NotNull Authentication auth) {
        log.debug("saveLike() called. Like: {}", likeDTO);
        Like like = likeRepository.save(getLike(likeDTO, auth));
        log.trace("saveLike(). Like saved: {}", like);
    }

    public void deleteLike(@NotNull LikeDTO likeDTO, @NotNull Authentication auth) {
        log.debug("deleteLike() called. Like: {}", likeDTO);
        likeRepository.delete(getLike(likeDTO, auth));
        log.trace("deleteLike(). Like deleted: {}", likeDTO);
    }

    private Like getLike(@NotNull LikeDTO likeDTO, @NotNull Authentication auth) {
        Optional<Article> articleOpt = articleQueryService.getArticle(likeDTO.articleID(), auth);
        if (articleOpt.isEmpty() || articleOpt.get().getState() == ArticleState.IN_EDITING) {
            log.warn("getLike(). Article with ID={} does not exist", likeDTO.articleID());
            throw new EntityNotFoundException("Article with required ID does not exist");
        }
        Article article = articleOpt.get();

        User user = userService.loadUserById(likeDTO.userID());
        User userFromAuth = userService.getUserFromAuthentication(auth);

        if (!user.equals(userFromAuth)) {
            log.warn("getLike(). User from auth does not have rights to update this like");
            throw new SecurityException("User does not have rights to update this like");
        }

        return new Like(user.getId(), article.getId(), user, article);
    }
}
