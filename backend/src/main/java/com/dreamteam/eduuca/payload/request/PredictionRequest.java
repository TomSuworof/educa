package com.dreamteam.eduuca.payload.request;

import com.dreamteam.eduuca.payload.common.InputDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PredictionRequest(InputDTO input) {
}
