package com.dreamteam.eduuca.payload.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record QuestionDTO(
        UUID id,
        UUID exerciseId,
        String answer,
        String remark,
        String hint
) {
}
