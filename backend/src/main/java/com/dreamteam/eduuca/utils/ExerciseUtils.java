package com.dreamteam.eduuca.utils;

import com.dreamteam.eduuca.payload.common.QuestionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExerciseUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean isQuestion(String str) {
        try {
            return toQuestion(str) != null;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    public static String toString(QuestionDTO question) {
        try {
            return objectMapper.writeValueAsString(question);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public static QuestionDTO toQuestion(String str) throws JsonProcessingException {
        return objectMapper.readValue(str, QuestionDTO.class);
    }
}
