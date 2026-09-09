package tech.magicbook.analytics.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tech.magicbook.analytics.api.entity.enums.AgeRange;

public record CreateEndUserRequest(@NotBlank String externalId, @NotNull AgeRange ageRange, String region) {
    
}
