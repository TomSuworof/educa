package com.dreamteam.eduuca.services.progress;

import com.dreamteam.eduuca.entities.article.Article;
import com.dreamteam.eduuca.entities.progress.Progress;
import com.dreamteam.eduuca.entities.progress.ProgressEnum;
import com.dreamteam.eduuca.entities.progress.ProgressId;
import com.dreamteam.eduuca.entities.user.User;
import com.dreamteam.eduuca.entities.user.role.RoleEnum;
import com.dreamteam.eduuca.payload.common.ProgressDTO;
import com.dreamteam.eduuca.repositories.ProgressRepository;
import com.dreamteam.eduuca.services.UserService;
import com.dreamteam.eduuca.services.article.query.ArticleQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProgressService {
    private final ProgressRepository progressRepository;
    private final ArticleQueryService articleQueryService;
    private final UserService userService;

    public void saveProgressEntity(@NotNull ProgressDTO progressDTO, @NotNull Authentication auth) {
        log.debug("saveProgress() called. ProgressDTO: {}", progressDTO);

        User userFromAuth = userService.getUserFromAuthentication(auth);
        User userFromRequest = userService.loadUserById(progressDTO.userID());
        if (!(userFromAuth.equals(userFromRequest) || userFromAuth.is(RoleEnum.ADMIN))) {
            log.warn("saveProgress(). Came request for saving progress for another user. This operation is illegal");
            throw new SecurityException("Illegal operation for saving progress of another user");
        }
        UUID userID = userFromRequest.getId();

        Optional<Article> articleOpt = articleQueryService.getArticle(progressDTO.articleID(), auth);
        if (articleOpt.isEmpty()) {
            log.warn("saveProgress(). Article with ID={} not found", progressDTO.articleID());
            throw new EntityNotFoundException("Article with required ID not found");
        }
        UUID articleID = articleOpt.get().getId();

        ProgressEnum progressEnum = ProgressEnum.fromString(progressDTO.progress());

        log.trace("saveProgress(). Saving progress for for user with ID={}, article with ID={}, with state={}", userID, articleID, progressEnum);
        saveProgressEntity(userID, articleID, progressEnum);
        log.trace("saveProgress(). Saved progress");
    }

    public void saveProgressEntity(@NotNull UUID userID, @NotNull UUID articleID, @NotNull ProgressEnum progressEnum) {
        log.debug("saveProgressEntity() called. Saving progress for for user with ID={}, article with ID={}, with state={}", userID, articleID, progressEnum);

        Progress progress = new Progress();
        progress.setUserID(userID);
        progress.setArticleID(articleID);
        progress.setProgress(progressEnum);

        progressRepository.save(progress);
        log.trace("saveProgressEntity(). Saved progress: {}", progress);
    }

    public @NotNull ProgressDTO getProgress(@NotNull UUID userID, @NotNull UUID articleID) {
        log.debug("getProgress() called. User ID={}, article ID={}", userID, articleID);
        Optional<Progress> progressOpt = getByUserIDAndArticleID(userID, articleID);
        if (progressOpt.isPresent()) {
            log.trace("getProgress(). Progress found: {}", progressOpt::get);
            Progress progress = progressOpt.get();
            return new ProgressDTO(progress);
        } else {
            log.trace("getProgress(). Progress not found. Going to save as '{}'", ProgressEnum.INCOMPLETE);
            saveProgressEntity(userID, articleID, ProgressEnum.INCOMPLETE);
            return new ProgressDTO(userID, articleID, ProgressEnum.INCOMPLETE.name());
        }
    }

    private Optional<Progress> getByUserIDAndArticleID(@NotNull UUID userID, @NotNull UUID articleID) {
        log.debug("getByUserIDAndArticleID() called. User ID={}, article ID={}", userID, articleID);
        return progressRepository.findById(new ProgressId(userID, articleID));
    }
}
