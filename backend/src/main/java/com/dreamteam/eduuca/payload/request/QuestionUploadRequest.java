package com.dreamteam.eduuca.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class QuestionUploadRequest {
    private UUID exerciseId;
    private String answer;
    private String remark;
    private String hint;
}
