package tech.magicbook.analytics.api.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tech.magicbook.analytics.api.dto.ApiResponse;
import tech.magicbook.analytics.api.dto.CreateEndUserRequest;
import tech.magicbook.analytics.api.dto.EndUserCreationResult;
import tech.magicbook.analytics.api.dto.EndUserResponse;
import tech.magicbook.analytics.api.dto.PaginatedResponse;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.entity.enums.AgeRange;
import tech.magicbook.analytics.api.entity.enums.EndUserStatus;
import tech.magicbook.analytics.api.service.ApiKeyAuthenticationService;
import tech.magicbook.analytics.api.service.EndUserService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/v1/end-users")
public class EndUserController {

    private final EndUserService endUserService;
    private final ApiKeyAuthenticationService apiKeyAuthenticationService;

    public EndUserController(EndUserService endUserService, ApiKeyAuthenticationService apiKeyAuthenticationService) {
        this.endUserService = endUserService;
        this.apiKeyAuthenticationService = apiKeyAuthenticationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EndUserResponse>> create(
            @RequestHeader(value = "X-Api-Key", required = false) String apiKey,
            @Valid @RequestBody CreateEndUserRequest request) {

        Application application = apiKeyAuthenticationService.authenticate(apiKey);

        EndUserCreationResult result = endUserService.create(application, request);

        ApiResponse<EndUserResponse> response = new ApiResponse<>(result.endUser());

        if (result.created()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{endUserId}")
    public ResponseEntity<Void> delete(@PathVariable UUID endUserId,
            @RequestHeader(value = "X-api-Key", required = false) String apiKey) {

        Application application = apiKeyAuthenticationService.authenticate(apiKey);

        endUserService.delete(application, endUserId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<EndUserResponse>> list(
            @RequestParam(required = false) UUID applicationId,
            @RequestParam(required = false) EndUserStatus status,
            @RequestParam(required = false) AgeRange ageRange,
            @RequestParam(required = false) String region,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order) {

        return ResponseEntity.ok(endUserService.list(applicationId, status, ageRange, region, page, limit, sort, order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EndUserResponse>> findById(@PathVariable UUID id) {

        EndUserResponse response = endUserService.findById(id);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

}
