package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.payload.common.LikeDTO;
import com.dreamteam.eduuca.services.like.LikeSaveService;
import com.dreamteam.eduuca.services.like.LikeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/api/likes")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class LikeController {
    private final LikeSaveService likeSaveService;
    private final LikeService likeService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void saveLike(@RequestBody LikeDTO likeDTO, Authentication auth) {
        log.debug("saveLike() called. Like: {}", likeDTO);
        likeSaveService.saveLike(likeDTO, auth);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Integer> getLikeCount(@RequestParam UUID articleID) {
        log.debug("getLikeCount() called. Article ID={}", articleID);
        Integer likeCount = likeService.getLikeCount(articleID);
        log.trace("getLikeCount(). Like count: {}", likeCount);
        return ResponseEntity.ok().body(likeCount);
    }
}
