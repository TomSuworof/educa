package com.dreamteam.eduuca.payload.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ExerciseUploadRequest {
    private UUID id;
    private String title;
    private String customUrl;
    private String content;
    private String solution;
}
