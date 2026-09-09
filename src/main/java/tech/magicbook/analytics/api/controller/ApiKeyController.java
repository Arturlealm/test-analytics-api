package tech.magicbook.analytics.api.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tech.magicbook.analytics.api.dto.ApiKeyResponse;
import tech.magicbook.analytics.api.dto.ApiResponse;
import tech.magicbook.analytics.api.dto.CreateApiKeyResponse;
import tech.magicbook.analytics.api.service.ApiKeyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController 
@RequestMapping ("/v1/applications/{applicationId}/api-keys")
public class ApiKeyController {
    
    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService){
        this.apiKeyService = apiKeyService;
    }

    @PostMapping    
    public ResponseEntity<ApiResponse<CreateApiKeyResponse>> create(@PathVariable UUID applicationId) {
        
        CreateApiKeyResponse response = apiKeyService.create(applicationId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ApiKeyResponse>>> findAll(@PathVariable UUID applicationId){

        List<ApiKeyResponse> response = apiKeyService.findAll(applicationId);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @DeleteMapping ("/{apiKeyId}")
    public ResponseEntity<Void> deactive(@PathVariable UUID applicationId, @PathVariable UUID apiKeyId){
        
        apiKeyService.deactivate(applicationId, apiKeyId);

        return ResponseEntity.noContent().build();
    }
    
}
