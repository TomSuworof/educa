package com.dreamteam.eduuca.controllers;

import com.dreamteam.eduuca.payload.common.ThemeDTO;
import com.dreamteam.eduuca.services.ThemeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class ThemeController {
    private final ThemeService themeService;

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ThemeDTO> getTheme(@RequestParam String themeName) {
        log.debug("getTheme() called. Theme name: {}", themeName);
        ThemeDTO response = themeService.getThemeByName(themeName);
        log.trace("getTheme(). Response to send: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/get_children")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<ThemeDTO>> getChildren(@RequestParam String themeName) {
        log.debug("getChildren() called. Theme name: {}", themeName);
        List<ThemeDTO> response = themeService.getChildrenThemes(themeName);
        log.trace("getChildren(). Response to send: {}", response);
        return ResponseEntity.ok().body(response);
    }

    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<ThemeDTO> saveTheme(@RequestBody ThemeDTO themeDTO) {
        log.debug("saveTheme() called. Theme: {}", themeDTO);
        themeService.saveTheme(themeDTO);
        log.trace("saveTheme(). Theme was saved, will return DTO back with OK");
        return ResponseEntity.ok().body(themeDTO);
    }
}
