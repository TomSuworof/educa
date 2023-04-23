package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.entities.article.theme.Theme;
import com.dreamteam.eduuca.payload.common.ThemeDTO;
import com.dreamteam.eduuca.repositories.ThemeRepository;
import com.dreamteam.eduuca.utils.OptionalUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class ThemeService {
    private final ThemeRepository themeRepository;

    public Theme getTheme(@NotNull UUID themeID) throws EntityNotFoundException {
        log.debug("getThemeByName() called. Theme ID={}", themeID);
        Optional<Theme> themeOpt = themeRepository.findById(themeID);
        if (themeOpt.isEmpty()) {
            log.warn("getThemeByName(). Theme with ID={} not found", themeID);
            throw new EntityNotFoundException("Theme with this ID not found");
        }
        return themeOpt.get();
    }

    public List<ThemeDTO> getChildrenThemes(@Nullable UUID parentThemeID) {
        log.debug("getChildrenThemes() called. Parent theme ID={}", parentThemeID);

        List<ThemeDTO> children;

        if (parentThemeID == null) {
            // getting root themes
            children = getAllThemes()
                    .stream()
                    .filter(theme -> theme.getParent() == null)
                    .map(ThemeDTO::new)
                    .toList();
        } else {
            // getting non-root themes
            Optional<Theme> parentThemeOpt = OptionalUtils.optTry(() -> getTheme(parentThemeID));
            if (parentThemeOpt.isEmpty()) {
                log.warn("getChildrenThemes(). Parent theme with ID={} not found", parentThemeID);
                throw new EntityNotFoundException("Theme with this ID does not exist");
            }
            Theme parentTheme = parentThemeOpt.get();

            children = getAllThemes()
                    .stream()
                    .filter(theme -> theme.getParent() != null && theme.getParent().getId().equals(parentTheme.getId()))
                    .map(ThemeDTO::new)
                    .toList();
        }

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
