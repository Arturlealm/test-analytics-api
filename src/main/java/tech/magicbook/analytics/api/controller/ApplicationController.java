package tech.magicbook.analytics.api.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import tech.magicbook.analytics.api.dto.ApiResponse;
import tech.magicbook.analytics.api.dto.ApplicationResponse;
import tech.magicbook.analytics.api.dto.CreateApplicationRequest;
import tech.magicbook.analytics.api.dto.PaginatedResponse;
import tech.magicbook.analytics.api.dto.PaginationMetadata;
import tech.magicbook.analytics.api.service.ApplicationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/v1/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ApplicationResponse>> create(
            @Valid @RequestBody CreateApplicationRequest request) {

        ApplicationResponse response = applicationService.create(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> findById(@PathVariable UUID id) {

        ApplicationResponse response = applicationService.findById(id);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<ApplicationResponse>> findAll(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order) {

        Page<ApplicationResponse> result = applicationService.findAll(page, limit, sort, order);

        PaginationMetadata pagination = new PaginationMetadata(
                page, limit, sort, order, result.getTotalElements(), result.getTotalPages());

        PaginatedResponse<ApplicationResponse> response = new PaginatedResponse<>(result.getContent(), pagination);

        return ResponseEntity.ok(response);
    }

}
