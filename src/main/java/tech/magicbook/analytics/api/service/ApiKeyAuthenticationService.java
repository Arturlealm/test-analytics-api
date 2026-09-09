package tech.magicbook.analytics.api.service;

import org.springframework.stereotype.Service;

import tech.magicbook.analytics.api.entity.ApiKey;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.exception.InvalidApiKeyException;
import tech.magicbook.analytics.api.repository.ApiKeyRepository;
import tech.magicbook.analytics.api.util.ApiKeyGenerator;

@Service
public class ApiKeyAuthenticationService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyAuthenticationService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public Application authenticate(String key) {

        if (key == null || key.isBlank()) {
            throw new InvalidApiKeyException();
        }

        String keyHash = ApiKeyGenerator.hash(key);

        ApiKey apiKey = apiKeyRepository
                .findByKeyHashAndActiveTrue(keyHash)
                .orElseThrow(() -> new InvalidApiKeyException());

        return apiKey.getApplication();
    }
}
