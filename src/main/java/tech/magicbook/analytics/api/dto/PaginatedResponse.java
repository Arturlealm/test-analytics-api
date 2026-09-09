package tech.magicbook.analytics.api.dto;

import java.util.List;

public record PaginatedResponse<T>(List<T> data, PaginationMetadata pagination) {
    
}
