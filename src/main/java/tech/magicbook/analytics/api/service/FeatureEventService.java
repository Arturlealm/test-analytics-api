package tech.magicbook.analytics.api.service;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import tech.magicbook.analytics.api.dto.CreateFeatureEventRequest;
import tech.magicbook.analytics.api.dto.FeatureEventResponse;
import tech.magicbook.analytics.api.dto.PaginatedResponse;
import tech.magicbook.analytics.api.dto.PaginationMetadata;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.entity.EndUser;
import tech.magicbook.analytics.api.entity.FeatureEvent;
import tech.magicbook.analytics.api.exception.EndUserNotFoundException;
import tech.magicbook.analytics.api.exception.InvalidRequestException;
import tech.magicbook.analytics.api.repository.EndUserRepository;
import tech.magicbook.analytics.api.repository.FeatureEventRepository;
import tech.magicbook.analytics.api.specification.FeatureEventSpecification;
import tech.magicbook.analytics.api.util.UuidGenerator;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class FeatureEventService {

    private final FeatureEventRepository featureEventRepository;
    private final EndUserRepository endUserRepository;

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "featureName", "occurredAt");

    public FeatureEventService(FeatureEventRepository featureEventRepository, EndUserRepository endUserRepository) {
        this.featureEventRepository = featureEventRepository;
        this.endUserRepository = endUserRepository;
    }

    public FeatureEventResponse create(Application application, CreateFeatureEventRequest request) {

        EndUser endUser = null;

        if (request.endUserId() != null) {
            endUser = endUserRepository.findById(request.endUserId()).orElseThrow(() -> new EndUserNotFoundException());
        }

        if (endUser != null && !endUser.getApplication().getId().equals(application.getId())) {
            throw new EndUserNotFoundException();
        }

        FeatureEvent featureEvent = new FeatureEvent(
                UuidGenerator.generateV7(),
                application,
                endUser,
                request.featureName(),
                request.occurredAt(),
                request.metadata());

        featureEventRepository.save(featureEvent);

        return toResponse(featureEvent);
    }

    public PaginatedResponse<FeatureEventResponse> list(UUID applicationId, UUID endUserId, String featureName,
            OffsetDateTime occurredFrom, OffsetDateTime occurredTo,
            int page, int limit, String sort, String order) {

        if (page < 1) {
            throw new InvalidRequestException("page must be greater than or equal to 1");
        }

        if (limit < 1 || limit > 100) {
            throw new InvalidRequestException("limit must be between 1 and 100");
        }

        if (!ALLOWED_SORT_FIELDS.contains(sort)) {
            throw new InvalidRequestException("invalid sort field");
        }

        if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
            throw new InvalidRequestException("order must be asc or desc");
        }

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

        Specification<FeatureEvent> specification = Specification.where(
                FeatureEventSpecification.withFilters(applicationId, endUserId, featureName, occurredFrom, occurredTo));

        Page<FeatureEvent> result = featureEventRepository.findAll(specification, pageable);

        List<FeatureEventResponse> events = result.getContent().stream().map(this::toResponse).toList();

        PaginationMetadata pagination = new PaginationMetadata(page, limit, sort, order, result.getTotalElements(),
                result.getTotalPages());

        return new PaginatedResponse<>(events, pagination);
    }

    private FeatureEventResponse toResponse(FeatureEvent featureEvent) {

        return new FeatureEventResponse(
                featureEvent.getId(),
                featureEvent.getApplication().getId(),
                featureEvent.getEndUser() != null ? featureEvent.getEndUser().getId() : null,
                featureEvent.getFeatureName(),
                featureEvent.getOccurredAt(),
                featureEvent.getMetadata());
    }
}
