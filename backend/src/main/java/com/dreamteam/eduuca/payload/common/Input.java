package com.dreamteam.eduuca.payload.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Input(String text) {
}
