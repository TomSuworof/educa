package com.dreamteam.eduuca.payload.response;

import java.util.List;

public record PageResponseDTO<T extends ObjectDTO>(
        boolean hasBefore,
        boolean hasAfter,
        long totalCount,
        List<T> entities
) {
}
