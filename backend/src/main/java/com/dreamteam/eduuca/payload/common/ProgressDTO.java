package com.dreamteam.eduuca.payload.common;

import com.dreamteam.eduuca.entities.progress.Progress;

import java.util.UUID;

public record ProgressDTO(UUID userID, UUID articleID, String progress) {
    public ProgressDTO(Progress progress) {
        this(
                progress.getUserID(),
                progress.getArticleID(),
                progress.getProgress().name()
        );
    }
}
