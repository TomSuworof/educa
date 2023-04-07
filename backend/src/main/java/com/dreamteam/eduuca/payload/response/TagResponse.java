package com.dreamteam.eduuca.payload.response;

import java.util.List;

public record TagResponse(
        String text,
        List<String> tags
) {
}
