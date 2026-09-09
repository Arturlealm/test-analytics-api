package tech.magicbook.analytics.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import tech.magicbook.analytics.api.entity.enums.AgeRange;
import tech.magicbook.analytics.api.entity.enums.EndUserStatus;

public record EndUserResponse(
        UUID id,
        UUID applicationId,
        String externalId,
        AgeRange ageRange,
        String region,
        EndUserStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime deletedAt) {

}
