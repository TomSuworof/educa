package com.dreamteam.eduuca.payload.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@RequiredArgsConstructor
public class QuestionDTO {
    private final UUID id;
    private final UUID exerciseId;
    private final String remark;
    private final String hint;
}
