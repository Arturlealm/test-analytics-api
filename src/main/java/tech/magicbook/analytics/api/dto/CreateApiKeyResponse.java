package tech.magicbook.analytics.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CreateApiKeyResponse(UUID id, UUID applicationId, String prefix, String key, boolean active, OffsetDateTime createdAt) {
    
}
