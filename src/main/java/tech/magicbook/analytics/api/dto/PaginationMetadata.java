package tech.magicbook.analytics.api.dto;

public record PaginationMetadata(int page, int limit, String sort, String order, long totalItems, int totalPages) {

}

