package tech.magicbook.analytics.api.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.magicbook.analytics.api.dto.AiUsageResponse;
import tech.magicbook.analytics.api.dto.ApiResponse;
import tech.magicbook.analytics.api.dto.CreateAiUsageRequest;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.service.AiUsageService;
import tech.magicbook.analytics.api.service.ApiKeyAuthenticationService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.OffsetDateTime;
import java.util.UUID;
import tech.magicbook.analytics.api.dto.PaginatedResponse;

@RestController
@RequestMapping("/v1/ai-usages")
public class AiUsageController {

    private final AiUsageService aiUsageService;
    private final ApiKeyAuthenticationService apiKeyAuthenticationService;

    public AiUsageController(AiUsageService aiUsageService, ApiKeyAuthenticationService apiKeyAuthenticationService) {
        this.aiUsageService = aiUsageService;
        this.apiKeyAuthenticationService = apiKeyAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AiUsageResponse>> create(
            @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
            @Valid @RequestBody CreateAiUsageRequest request) {

        Application application = apiKeyAuthenticationService.authenticate(apiKey);

        AiUsageResponse aiUsage = aiUsageService.create(application, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(aiUsage));

    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<AiUsageResponse>> list(
            @RequestParam(required = false) UUID applicationId,
            @RequestParam(required = false) UUID endUserId,
            @RequestParam(required = false) String provider,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) OffsetDateTime occurredFrom,
            @RequestParam(required = false) OffsetDateTime occurredTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "occurredAt") String sort,
            @RequestParam(defaultValue = "desc") String order) {

        return ResponseEntity.ok(aiUsageService.list(applicationId, endUserId, provider, model, occurredFrom,
                occurredTo, page, limit, sort, order));
    }
}
