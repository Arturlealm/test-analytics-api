package tech.magicbook.analytics.api.dto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateFeatureEventRequest(UUID endUserId, @NotBlank String featureName, @NotNull OffsetDateTime occurredAt, Map<String, Object> metadata) {
    
}
