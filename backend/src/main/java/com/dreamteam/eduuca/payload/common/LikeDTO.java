package com.dreamteam.eduuca.payload.common;

import com.dreamteam.eduuca.entities.article.like.Like;

import java.util.UUID;

public record LikeDTO(UUID userID, UUID articleID) {
    public LikeDTO(Like like) {
        this(like.getUserID(), like.getUserID());
    }
}
