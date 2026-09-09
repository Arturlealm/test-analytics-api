package tech.magicbook.analytics.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateAiUsageRequest(
    UUID endUserId, 
    @NotBlank String provider,
    @NotBlank String model,
    @NotNull @PositiveOrZero Long tokens,
    @NotNull @PositiveOrZero BigDecimal cost,
    @NotNull OffsetDateTime occurredAt
) {
    
}
