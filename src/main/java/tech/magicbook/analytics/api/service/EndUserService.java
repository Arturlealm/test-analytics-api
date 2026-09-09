package tech.magicbook.analytics.api.service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.magicbook.analytics.api.dto.CreateEndUserRequest;
import tech.magicbook.analytics.api.dto.EndUserCreationResult;
import tech.magicbook.analytics.api.dto.EndUserResponse;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.entity.EndUser;
import tech.magicbook.analytics.api.entity.enums.EndUserStatus;
import tech.magicbook.analytics.api.exception.EndUserNotFoundException;
import tech.magicbook.analytics.api.repository.EndUserRepository;
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

    public void delete(Application application, UUID endUserId){

        EndUser endUser = endUserRepository.findById(endUserId).orElseThrow(() -> new EndUserNotFoundException());

        if (!endUser.getApplication().getId().equals(application.getId())) {
            throw new EndUserNotFoundException();
        }

        endUser.delete();

        endUserRepository.save(endUser);
    }
}
