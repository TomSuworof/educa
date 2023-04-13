package com.dreamteam.eduuca.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class ExerciseUploadRequest {
    private UUID id;
    private String title;
    private String customUrl;
    private String content;
    private String solution;
    private List<String> tags;
}
