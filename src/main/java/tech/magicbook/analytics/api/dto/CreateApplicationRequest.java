package tech.magicbook.analytics.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateApplicationRequest(@NotBlank @Size (max = 255) String name, String description){
}
