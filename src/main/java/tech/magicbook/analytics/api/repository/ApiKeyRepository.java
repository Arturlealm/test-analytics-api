package tech.magicbook.analytics.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.magicbook.analytics.api.entity.ApiKey;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {

    List<ApiKey> findByApplicationId(UUID applicationId);
    
}