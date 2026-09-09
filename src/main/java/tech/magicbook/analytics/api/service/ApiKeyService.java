package tech.magicbook.analytics.api.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import tech.magicbook.analytics.api.dto.ApiKeyResponse;
import tech.magicbook.analytics.api.dto.CreateApiKeyResponse;
import tech.magicbook.analytics.api.entity.ApiKey;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.exception.ApiKeyNotFoundException;
import tech.magicbook.analytics.api.exception.ApplicationNotFoundException;
import tech.magicbook.analytics.api.repository.ApiKeyRepository;
import tech.magicbook.analytics.api.repository.ApplicationRepository;
import tech.magicbook.analytics.api.util.ApiKeyGenerator;
import tech.magicbook.analytics.api.util.UuidGenerator;

@Service 
public class ApiKeyService {
    
    private final ApiKeyRepository apiKeyRepository;
    private final ApplicationRepository applicationRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository, ApplicationRepository applicationRepository){
        this.apiKeyRepository = apiKeyRepository;
        this.applicationRepository = applicationRepository;
    }

    public CreateApiKeyResponse create(UUID applicationId){

        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException((applicationId)));

        String key = ApiKeyGenerator.generate();
        String prefix = key.substring(0,11);
        String keyHash = ApiKeyGenerator.hash(key);

        ApiKey apiKey = new ApiKey(UuidGenerator.generateV7(), application, prefix, keyHash, true, OffsetDateTime.now());

        apiKeyRepository.save(apiKey);

        return new CreateApiKeyResponse(
            apiKey.getId(),
            application.getId(),
            apiKey.getPrefix(),
            key,
            apiKey.isActive(),
            apiKey.getCreatedAt()
        );
    }

    public List<ApiKeyResponse> findAll(UUID applicationId){
        
        if (!applicationRepository.existsById(applicationId)) {
            throw new ApplicationNotFoundException(applicationId);
        }

        return apiKeyRepository.findByApplicationId(applicationId).stream().map(apiKey -> new ApiKeyResponse(
            apiKey.getId(),
            apiKey.getApplication().getId(),
            apiKey.getPrefix(),
            apiKey.isActive(),
            apiKey.getCreatedAt()
        ))
        .toList();
    }

    public void deactivate(UUID applicationId, UUID apiKeyId){
        
        ApiKey apiKey = apiKeyRepository.findById(apiKeyId).orElseThrow(() -> new ApiKeyNotFoundException(apiKeyId));

        if (!apiKey.getApplication().getId().equals(applicationId)) {
            throw new ApiKeyNotFoundException(apiKeyId);
        }

        apiKey.deactivate();

        apiKeyRepository.save(apiKey);
    }
}
