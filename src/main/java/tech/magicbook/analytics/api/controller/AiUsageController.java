package tech.magicbook.analytics.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tech.magicbook.analytics.api.dto.AiUsageResponse;
import tech.magicbook.analytics.api.dto.ApiResponse;
import tech.magicbook.analytics.api.dto.CreateAiUsageRequest;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.service.AiUsageService;
import tech.magicbook.analytics.api.service.ApiKeyAuthenticationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/v1/ai-usages")
public class AiUsageController {

    private final AiUsageService aiUsageService;
    private final ApiKeyAuthenticationService apiKeyAuthenticationService;

    public AiUsageController(AiUsageService aiUsageService, ApiKeyAuthenticationService apiKeyAuthenticationService){
        this.aiUsageService = aiUsageService;
        this.apiKeyAuthenticationService = apiKeyAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AiUsageResponse>> create(
        @RequestHeader(value = "X-Api-Key", required = false) String apiKey, @Valid @RequestBody CreateAiUsageRequest request){
            
            Application application = apiKeyAuthenticationService.authenticate(apiKey);

            AiUsageResponse aiUsage = aiUsageService.create(application, request);

            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(aiUsage));

        }
    
}
