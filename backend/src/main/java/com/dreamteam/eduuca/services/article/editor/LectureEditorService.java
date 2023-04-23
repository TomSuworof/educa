package com.dreamteam.eduuca.services.article.editor;

import com.dreamteam.eduuca.entities.article.lecture.Lecture;
import com.dreamteam.eduuca.payload.request.LectureUploadRequest;
import com.dreamteam.eduuca.payload.response.article.lecture.LectureFullDTO;
import com.dreamteam.eduuca.services.TagService;
import com.dreamteam.eduuca.services.UserService;
import com.dreamteam.eduuca.services.article.ArticleSaveService;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LectureEditorService extends ArticleEditorService<Lecture, LectureUploadRequest, LectureFullDTO> {
    public LectureEditorService(UserService userService, ArticleSaveService articleSaveService, TagService tagService) {
        super(userService, articleSaveService, tagService);
    }

    @Override
    protected @NotNull Lecture newBlankEntity() {
        return new Lecture();
    }

    @Override
    protected void enrichFromRequest(@NotNull Lecture lecture, @NotNull LectureUploadRequest uploadRequest) {
        lecture.setTitle(uploadRequest.getTitle());
        lecture.setCustomUrl(uploadRequest.getCustomUrl());
        lecture.setSummary(uploadRequest.getSummary());
        lecture.setContent(uploadRequest.getContent());
    }

    @Override
    protected @NotNull LectureFullDTO entityToDTO(@NotNull Lecture lecture) {
        return new LectureFullDTO(lecture);
    }

    @Override
    protected void postProcess(@NotNull Lecture article, @NotNull Authentication auth) {
        // do nothing
    }
}
