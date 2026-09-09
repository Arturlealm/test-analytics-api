package tech.magicbook.analytics.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApiKeyResponse(UUID id, UUID applicationId, String prefix, boolean active, OffsetDateTime createdAt) {
    
}
