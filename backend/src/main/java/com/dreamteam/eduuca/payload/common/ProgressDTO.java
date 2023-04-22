package com.dreamteam.eduuca.payload.common;

import java.util.UUID;

public record ProgressDTO(UUID userID, UUID articleID, String progress) {
}
