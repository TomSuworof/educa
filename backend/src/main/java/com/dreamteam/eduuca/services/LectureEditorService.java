package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Lecture;
import com.dreamteam.eduuca.payload.request.LectureUploadRequest;
import com.dreamteam.eduuca.payload.response.article.lecture.LectureFullDTO;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class LectureEditorService extends ArticleEditorService<Lecture, LectureUploadRequest, LectureFullDTO> {
    public LectureEditorService(UserService userService, ArticleService articleService, TagService tagService) {
        super(userService, articleService, tagService);
    }

    @Override
    protected @NotNull Lecture newBlankEntity() {
        return new Lecture();
    }

    @Override
    protected void enrichFromRequest(Lecture lecture, LectureUploadRequest uploadRequest) {
        lecture.setTitle(uploadRequest.getTitle());
        lecture.setCustomUrl(uploadRequest.getCustomUrl());
        lecture.setSummary(uploadRequest.getSummary());
        lecture.setContent(uploadRequest.getContent());
    }

    @Override
    protected @NotNull LectureFullDTO entityToDTO(Lecture lecture) {
        return new LectureFullDTO(lecture);
    }
}
