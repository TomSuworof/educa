package com.dreamteam.eduuca.entities.progress;

import org.jetbrains.annotations.NotNull;

public enum ProgressEnum {
    INCOMPLETE,
    COMPLETE;

    public static ProgressEnum fromString(@NotNull String progressString) throws IllegalArgumentException {
        for (ProgressEnum p : values()) {
            if (p.name().equalsIgnoreCase(progressString)) {
                return p;
            }
        }
        throw new IllegalArgumentException(String.format("Progress cannot be parsed from string '%s'", progressString));
    }
}
