package tech.magicbook.analytics.api.specification;

import java.util.UUID;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import tech.magicbook.analytics.api.entity.EndUser;
import tech.magicbook.analytics.api.entity.enums.AgeRange;
import tech.magicbook.analytics.api.entity.enums.EndUserStatus;

public class EndUserSpecification {

    public static Specification<EndUser> withFilters(UUID applicationId, EndUserStatus status, AgeRange ageRange,
            String region) {

        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (applicationId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("application").get("id"), applicationId));
            }

            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }

            if (ageRange != null) {
                predicate = cb.and(predicate, cb.equal(root.get("ageRange"), ageRange));
            }

            if (region != null && !region.isBlank()) {
                predicate = cb.and(predicate, cb.equal(root.get("region"), region));
            }

            return predicate;
        };
    }
}