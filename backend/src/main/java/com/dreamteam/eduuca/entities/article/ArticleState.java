package com.dreamteam.eduuca.entities.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum ArticleState {
    PUBLISHED("publish", "published"),
    IN_EDITING("save", "drafts"),
    ALL(null, "all");

    private final String actionForThisState;
    private final String description;

    public static ArticleState getFromAction(String action) {
        for (ArticleState state : values()) {
            // if null we should not do anything - just skip it
            if (state.getActionForThisState() != null && state.getActionForThisState().equalsIgnoreCase(action)) {
                return state;
            }
        }
        throw new IllegalStateException("State does not exist");
    }

    public static ArticleState getFromDescription(String description) {
        for (ArticleState state : values()) {
            if (state.getDescription().equalsIgnoreCase(description)) {
                return state;
            }
        }
        throw new IllegalStateException("State does not exist");
    }
}