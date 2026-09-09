package tech.magicbook.analytics.api.specification;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import tech.magicbook.analytics.api.entity.AiUsage;

public class AiUsageSpecification {

    public static Specification<AiUsage> withFilters(UUID applicationId, UUID endUserId, String provider, String model,
            OffsetDateTime occurredFrom, OffsetDateTime occurredTo) {

        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (applicationId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("application").get("id"), applicationId));
            }

            if (endUserId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("endUser").get("id"), endUserId));
            }

            if (provider != null && !provider.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("provider"), provider));
            }

            if (model != null && !model.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("model"), model));
            }

            if (occurredFrom != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("occurredAt"), occurredFrom));
            }

            if (occurredTo != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("occurredAt"), occurredTo));
            }

            return predicate;
        };
    }
}