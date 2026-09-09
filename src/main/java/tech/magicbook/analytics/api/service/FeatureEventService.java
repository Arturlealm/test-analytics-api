package tech.magicbook.analytics.api.service;

import org.springframework.stereotype.Service;

import tech.magicbook.analytics.api.dto.CreateFeatureEventRequest;
import tech.magicbook.analytics.api.dto.FeatureEventResponse;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.entity.EndUser;
import tech.magicbook.analytics.api.entity.FeatureEvent;
import tech.magicbook.analytics.api.exception.EndUserNotFoundException;
import tech.magicbook.analytics.api.repository.EndUserRepository;
import tech.magicbook.analytics.api.repository.FeatureEventRepository;
import tech.magicbook.analytics.api.util.UuidGenerator;

@Service
public class FeatureEventService {

    private final FeatureEventRepository featureEventRepository;
    private final EndUserRepository endUserRepository;

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

        return new FeatureEventResponse(
            featureEvent.getId(),
            application.getId(),
            endUser != null ? endUser.getId() : null,
            featureEvent.getFeatureName(),
            featureEvent.getOccurredAt(),
            featureEvent.getMetadata()
        );
    }
}
