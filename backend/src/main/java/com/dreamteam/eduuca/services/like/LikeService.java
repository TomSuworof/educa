package com.dreamteam.eduuca.services.like;

import com.dreamteam.eduuca.repositories.LikeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;

    public Integer getLikeCount(@NotNull UUID articleID) {
        log.debug("getLikeCount() called. Article ID={}", articleID);
        return likeRepository.countByArticle_Id(articleID);
    }
}
