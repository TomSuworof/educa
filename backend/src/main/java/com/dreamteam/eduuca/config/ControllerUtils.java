package com.dreamteam.eduuca.config;

import com.dreamteam.eduuca.payload.response.ObjectDTO;
import com.dreamteam.eduuca.payload.response.PageResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Log4j2
public class ControllerUtils {
    public static <T extends ObjectDTO> ResponseEntity<PageResponseDTO<T>> processPartialResponse(PageResponseDTO<T> response) {
        if (!response.isHasBefore() && !response.isHasAfter()) {
            log.trace("processPartialResponse(). Response contains all entities. Response status is OK");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            log.trace("processPartialResponse(). Response does not contain all entities. Response status is PARTIAL_CONTENT");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }
    }
}
