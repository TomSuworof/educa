package com.dreamteam.eduuca.payload.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class AnswerResponse {
    private UUID questionId;
    private String answer;
    private String verdict;
}
