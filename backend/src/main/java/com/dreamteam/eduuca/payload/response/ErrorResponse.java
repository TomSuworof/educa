package com.dreamteam.eduuca.payload.response;

import java.util.Date;

public record ErrorResponse(Date timestamp, Integer status, String error, String path) {
}
