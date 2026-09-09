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

@Service 
public class AiUsageService {
    
    private final AiUsageRepository aiUsageRepository;
    private final EndUserRepository endUserRepository;

    public AiUsageService(AiUsageRepository aiUsageRepository, EndUserRepository endUserRepository){
        this.aiUsageRepository = aiUsageRepository;
        this.endUserRepository = endUserRepository;
    }

    public AiUsageResponse create(Application application, CreateAiUsageRequest request){

        EndUser endUser = null;

        if (request.endUserId() != null) {
            
            endUser = endUserRepository.findById((request.endUserId())).orElseThrow(() -> new EndUserNotFoundException());

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
            request.occurredAt()
        );

        aiUsageRepository.save(aiUsage);

        return new AiUsageResponse(
            aiUsage.getId(),
            application.getId(),
            endUser != null ? endUser.getId() : null,
            aiUsage.getProvider(),
            aiUsage.getModel(),
            aiUsage.getTokens(),
            aiUsage.getCost(),
            aiUsage.getOccurredAt()
        );
    }

}
