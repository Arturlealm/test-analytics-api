package tech.magicbook.analytics.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AiUsageResponse(UUID id, UUID applicationId, UUID endUserId, String provider, String model, Long tokens, BigDecimal cost, OffsetDateTime occurredAt) {
    
}
