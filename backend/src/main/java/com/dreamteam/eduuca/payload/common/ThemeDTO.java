package com.dreamteam.eduuca.payload.common;

import com.dreamteam.eduuca.entities.Theme;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public record ThemeDTO(
        UUID id,
        @Nullable UUID parentID,
        String name
) {
    public ThemeDTO(Theme theme) {
        this(
                theme.getId(),
                Optional.ofNullable(theme.getParent()).map(Theme::getId).orElse(null),
                theme.getName()
        );
    }
}
