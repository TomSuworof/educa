package com.dreamteam.eduuca.payload.request;

import com.dreamteam.eduuca.payload.common.Input;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PredictionRequest(Input input) {
}
