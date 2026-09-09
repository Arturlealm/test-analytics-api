package tech.magicbook.analytics.api.service;

import org.springframework.stereotype.Service;

import tech.magicbook.analytics.api.dto.AiUsageResponse;
import tech.magicbook.analytics.api.dto.CreateAiUsageRequest;
import tech.magicbook.analytics.api.entity.AiUsage;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.entity.EndUser;
import tech.magicbook.analytics.api.exception.EndUserNotFoundException;
import tech.magicbook.analytics.api.repository.AiUsageRepository;
import tech.magicbook.analytics.api.repository.EndUserRepository;
import tech.magicbook.analytics.api.util.UuidGenerator;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import tech.magicbook.analytics.api.dto.PaginatedResponse;
import tech.magicbook.analytics.api.dto.PaginationMetadata;
import tech.magicbook.analytics.api.specification.AiUsageSpecification;

@Service
public class AiUsageService {

    private final AiUsageRepository aiUsageRepository;
    private final EndUserRepository endUserRepository;

    public AiUsageService(AiUsageRepository aiUsageRepository, EndUserRepository endUserRepository) {
        this.aiUsageRepository = aiUsageRepository;
        this.endUserRepository = endUserRepository;
    }

    public AiUsageResponse create(Application application, CreateAiUsageRequest request) {

        EndUser endUser = null;

        if (request.endUserId() != null) {

            endUser = endUserRepository.findById((request.endUserId()))
                    .orElseThrow(() -> new EndUserNotFoundException());

            if (!endUser.getApplication().getId().equals(application.getId())) {
                throw new EndUserNotFoundException();
            }
        }

        AiUsage aiUsage = new AiUsage(
                UuidGenerator.generateV7(),
                application,
                endUser,
                request.provider(),
                request.model(),
                request.tokens(),
                request.cost(),
                request.occurredAt());

        aiUsageRepository.save(aiUsage);

        return toResponse(aiUsage);
    }

    public PaginatedResponse<AiUsageResponse> list(
            UUID applicationId,
            UUID endUserId,
            String provider,
            String model,
            OffsetDateTime occurredFrom,
            OffsetDateTime occurredTo,
            int page,
            int limit,
            String sort,
            String order) {

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

        Specification<AiUsage> specification = Specification.where(
                AiUsageSpecification.withFilters(applicationId, endUserId, provider, model, occurredFrom, occurredTo));

        Page<AiUsage> result = aiUsageRepository.findAll(specification, pageable);

        List<AiUsageResponse> usages = result.getContent().stream().map(this::toResponse).toList();

        PaginationMetadata pagination = new PaginationMetadata(page, limit, sort, order, result.getTotalElements(),
                result.getTotalPages());

        return new PaginatedResponse<>(usages, pagination);
    }

    private AiUsageResponse toResponse(AiUsage aiUsage) {

        return new AiUsageResponse(
                aiUsage.getId(),
                aiUsage.getApplication().getId(),
                aiUsage.getEndUser() != null ? aiUsage.getEndUser().getId() : null,
                aiUsage.getProvider(),
                aiUsage.getModel(),
                aiUsage.getTokens(),
                aiUsage.getCost(),
                aiUsage.getOccurredAt());
    }

}
