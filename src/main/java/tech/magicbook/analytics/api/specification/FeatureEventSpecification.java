package tech.magicbook.analytics.api.specification;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import tech.magicbook.analytics.api.entity.FeatureEvent;

public class FeatureEventSpecification {

    public static Specification<FeatureEvent> withFilters(UUID applicationId, UUID endUserId, String featureName,
            OffsetDateTime occurredFrom, OffsetDateTime occurredTo) {

        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (applicationId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("application").get("id"), applicationId));
            }

            if (endUserId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("endUser").get("id"), endUserId));
            }

            if (featureName != null && !featureName.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("featureName"), featureName));
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