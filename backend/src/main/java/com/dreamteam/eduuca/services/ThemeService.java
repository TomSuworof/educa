package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.Theme;
import com.dreamteam.eduuca.payload.common.ThemeDTO;
import com.dreamteam.eduuca.repositories.ThemeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;

    public ThemeDTO getThemeByName(@NotNull String themeName) throws EntityNotFoundException {
        log.debug("getThemeByName() called. Theme name: '{}'", themeName);
        Optional<Theme> themeOpt = themeRepository.findByName(themeName);
        if (themeOpt.isEmpty()) {
            log.warn("getThemeByName(). Theme with name '{}' not found", themeName);
            throw new EntityNotFoundException("Theme with this name not found");
        }
        return new ThemeDTO(themeOpt.get());
    }

    public List<ThemeDTO> getChildrenThemes(@NotNull String parentThemeName) {
        log.debug("getChildrenThemes() called. Parent theme name={}", parentThemeName);

        Optional<Theme> parentThemeOpt = themeRepository.findByName(parentThemeName);
        if (parentThemeOpt.isEmpty()) {
            log.warn("getChildrenThemes(). Parent theme with name '{}' not found", parentThemeName);
            throw new EntityNotFoundException("Theme with this name does not exist");
        }
        Theme parentTheme = parentThemeOpt.get();

        List<ThemeDTO> children = getAllThemes()
                .stream()
                .filter(theme -> theme.getParent() != null && theme.getParent().getId().equals(parentTheme.getId()))
                .map(ThemeDTO::new)
                .toList();
        log.trace("getChildrenThemes(). Children themes count: {}", children::size);
        return children;
    }

    private List<Theme> getAllThemes() {
        log.debug("getAllThemes() called.");
        return themeRepository.findAll();
    }

    public void saveTheme(@NotNull ThemeDTO themeDTO) {
        log.debug("saveTheme() called. Theme: {}", themeDTO);

        Theme parentTheme = null;

        if (themeDTO.parentID() != null) {
            Optional<Theme> parentThemeOpt = themeRepository.findById(themeDTO.parentID());
            if (parentThemeOpt.isEmpty()) {
                log.warn("saveTheme(). Parent theme with ID={} not found", themeDTO.parentID());
                throw new EntityNotFoundException("Parent theme with required ID not found");
            }
            parentTheme = parentThemeOpt.get();
        }

        Theme theme = new Theme();
        theme.setId(themeDTO.id());
        theme.setParent(parentTheme);
        theme.setName(themeDTO.name());

        log.trace("saveTheme(). Saving theme: {}", theme);
        themeRepository.save(theme);
        log.trace("saveTheme(). Theme saved");
    }
}
