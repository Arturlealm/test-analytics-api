package tech.magicbook.analytics.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import tech.magicbook.analytics.api.dto.ApiResponse;
import tech.magicbook.analytics.api.dto.CreateFeatureEventRequest;
import tech.magicbook.analytics.api.dto.FeatureEventResponse;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.service.ApiKeyAuthenticationService;
import tech.magicbook.analytics.api.service.FeatureEventService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;
import java.util.UUID;

import tech.magicbook.analytics.api.dto.PaginatedResponse;

@RestController
@RequestMapping("/v1/feature-events")
public class FeatureEventController {

    private final FeatureEventService featureEventService;
    private final ApiKeyAuthenticationService apiKeyAuthenticationService;

    public FeatureEventController(FeatureEventService featureEventService,
            ApiKeyAuthenticationService apiKeyAuthenticationService) {
        this.featureEventService = featureEventService;
        this.apiKeyAuthenticationService = apiKeyAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FeatureEventResponse>> create(
            @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
            @Valid @RequestBody CreateFeatureEventRequest request) {

        Application application = apiKeyAuthenticationService.authenticate(apiKey);

        FeatureEventResponse featureEvent = featureEventService.create(application, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(featureEvent));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<FeatureEventResponse>> list(
            @RequestParam(required = false) UUID applicationId,
            @RequestParam(required = false) UUID endUserId,
            @RequestParam(required = false) String featureName,
            @RequestParam(required = false) OffsetDateTime occurredFrom,
            @RequestParam(required = false) OffsetDateTime occurredTo,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "occurredAt") String sort,
            @RequestParam(defaultValue = "desc") String order) {

        return ResponseEntity.ok(featureEventService.list(applicationId, endUserId, featureName, occurredFrom,
                occurredTo, page, limit, sort, order));
    }

}
