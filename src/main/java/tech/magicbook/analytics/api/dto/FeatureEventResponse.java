package tech.magicbook.analytics.api.dto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record FeatureEventResponse(UUID id, UUID applicationId, UUID endUserId, String featureName, OffsetDateTime occurredAt, Map<String, Object> metadata) {
    
}
