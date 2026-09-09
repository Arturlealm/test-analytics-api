package tech.magicbook.analytics.api.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ApplicationResponse(UUID id, String name, String description, OffsetDateTime createdAt){
    
}
