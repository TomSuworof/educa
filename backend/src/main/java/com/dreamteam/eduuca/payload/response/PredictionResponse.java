package com.dreamteam.eduuca.payload.response;

import com.dreamteam.eduuca.payload.common.Input;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PredictionResponse(
        Input input,
        List<String> output,
        String id,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("started_at") String startedAt,
        @JsonProperty("completed_at") String completedAt,
        String logs,
        String error,
        String status,
        @JsonProperty("webhook_events_filter") List<String> webhookEventsFilter,
        String webhook, @JsonProperty("output_file_prefix") String outputFilePrefix
) {
}
