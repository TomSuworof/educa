package com.dreamteam.eduuca.services.article.query;

import com.dreamteam.eduuca.entities.article.lecture.Lecture;
import com.dreamteam.eduuca.payload.response.article.lecture.LectureShortDTO;
import com.dreamteam.eduuca.repositories.ArticleRepository;
import com.dreamteam.eduuca.services.like.LikeService;
import com.dreamteam.eduuca.services.TagService;
import com.dreamteam.eduuca.services.ThemeService;
import com.dreamteam.eduuca.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LectureQueryService extends AbstractArticleQueryService<Lecture, LectureShortDTO> {
    public LectureQueryService(
            UserService userService,
            TagService tagService,
            ThemeService themeService,
            LikeService likeService,
            ArticleRepository<Lecture> articleRepository
    ) {
        super(userService, tagService, themeService, likeService, articleRepository);
    }

    @Override
    protected @Nullable LectureShortDTO parseToDTO(@NotNull Lecture lecture, @Nullable Integer likeCount) {
        return new LectureShortDTO(lecture, likeCount);
    }
}
