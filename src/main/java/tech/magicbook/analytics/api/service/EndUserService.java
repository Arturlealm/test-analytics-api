package tech.magicbook.analytics.api.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import tech.magicbook.analytics.api.dto.CreateEndUserRequest;
import tech.magicbook.analytics.api.dto.EndUserCreationResult;
import tech.magicbook.analytics.api.dto.EndUserResponse;
import tech.magicbook.analytics.api.dto.PaginatedResponse;
import tech.magicbook.analytics.api.dto.PaginationMetadata;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.entity.EndUser;
import tech.magicbook.analytics.api.entity.enums.AgeRange;
import tech.magicbook.analytics.api.entity.enums.EndUserStatus;
import tech.magicbook.analytics.api.exception.EndUserNotFoundException;
import tech.magicbook.analytics.api.repository.EndUserRepository;
import tech.magicbook.analytics.api.specification.EndUserSpecification;
import tech.magicbook.analytics.api.util.UuidGenerator;

@Service
public class EndUserService {

    private final EndUserRepository endUserRepository;

    public EndUserService(EndUserRepository endUserRepository) {
        this.endUserRepository = endUserRepository;
    }

    public EndUserCreationResult create(Application application, CreateEndUserRequest request) {

        Optional<EndUser> existingEndUser = endUserRepository.findByApplicationIdAndExternalId(application.getId(),
                request.externalId());

        if (existingEndUser.isPresent()) {
            return new EndUserCreationResult(toResponse(existingEndUser.get()), false);
        }

        EndUser endUser = new EndUser(
                UuidGenerator.generateV7(),
                application,
                request.externalId(),
                request.ageRange(),
                request.region(),
                EndUserStatus.ACTIVE,
                OffsetDateTime.now());

        endUserRepository.save(endUser);

        return new EndUserCreationResult(toResponse(endUser), true);
    }

    private EndUserResponse toResponse(EndUser endUser) {

        return new EndUserResponse(
                endUser.getId(),
                endUser.getApplication().getId(),
                endUser.getExternalId(),
                endUser.getAgeRange(),
                endUser.getRegion(),
                endUser.getStatus(),
                endUser.getCreatedAt(),
                endUser.getDeletedAt()

        );
    }

    public void delete(Application application, UUID endUserId) {

        EndUser endUser = endUserRepository.findById(endUserId).orElseThrow(() -> new EndUserNotFoundException());

        if (!endUser.getApplication().getId().equals(application.getId())) {
            throw new EndUserNotFoundException();
        }

        endUser.delete();

        endUserRepository.save(endUser);
    }

    public PaginatedResponse<EndUserResponse> list(UUID applicationId, EndUserStatus status, AgeRange ageRange,
            String region, int page, int limit, String sort, String order) {

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

        Specification<EndUser> specification = Specification
                .where(EndUserSpecification.withFilters(applicationId, status, ageRange, region));

        Page<EndUser> result = endUserRepository.findAll(specification, pageable);

        List<EndUserResponse> users = result.getContent().stream().map(this::toResponse).toList();

        PaginationMetadata pagination = new PaginationMetadata(page, limit, sort, order, result.getTotalElements(),
                result.getTotalPages());

        return new PaginatedResponse<>(users, pagination);
    }

    public EndUserResponse findById(UUID id) {

        EndUser endUser = endUserRepository.findById(id).orElseThrow(EndUserNotFoundException::new);

        return toResponse(endUser);
    }
}
