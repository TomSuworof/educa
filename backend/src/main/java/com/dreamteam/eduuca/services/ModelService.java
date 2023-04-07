package com.dreamteam.eduuca.services;

import com.dreamteam.eduuca.config.WebUtils;
import com.dreamteam.eduuca.payload.common.Input;
import com.dreamteam.eduuca.payload.request.PredictionRequest;
import com.dreamteam.eduuca.payload.response.PredictionResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
public class ModelService {
    @Value("${eduuca.tag-predictions.models.sklearn}")
    private String eduucaSklearnModelUrl;

    @Value("${eduuca.tag-predictions.models.yake}")
    private String eduucaYakeModelUrl;

    public List<String> getTags(String text) {
        log.debug("getTags() called. Text: {}", () -> text);
        List<String> tagsSklearn = getPredictionOutput(text, eduucaSklearnModelUrl);
        List<String> tagsYake = getPredictionOutput(text, eduucaYakeModelUrl);

        log.trace("getTags(). From sklearn: {}", () -> tagsSklearn);
        log.trace("getTags(). From yake: {}", () -> tagsYake);

        Set<String> result = new HashSet<>();
        result.addAll(tagsSklearn);
        result.addAll(tagsYake);

        log.trace("getTags(). Result: {}", () -> result);

        return result.stream().toList();
    }

    private List<String> getPredictionOutput(String text, String url) {
        log.debug("getPredictionOutput() called. Text: {}, url: {}", () -> text, () -> url);
        try {
            PredictionRequest predictionRequest = new PredictionRequest(new Input(text));
            URI uri = URI.create(url);

            PredictionResponse response = WebUtils.post(uri, predictionRequest, PredictionResponse.class);
            log.trace("getPredictionOutput(). Response: {}", () -> response);

            return response.output();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
