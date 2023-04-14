package com.dreamteam.eduuca.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public abstract class ArticleUploadRequest {
    protected UUID id;
    protected String title;
    protected String customUrl;
    protected String content;
    protected List<String> tags;
}
