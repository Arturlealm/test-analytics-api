package tech.magicbook.analytics.api.dto;

public record TokenResponse(String accessToken, String tokenType, long expiresIn) {
    
}
