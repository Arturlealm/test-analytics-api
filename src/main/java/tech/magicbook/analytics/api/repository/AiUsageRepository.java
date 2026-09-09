package tech.magicbook.analytics.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.magicbook.analytics.api.entity.AiUsage;

public interface AiUsageRepository extends JpaRepository<AiUsage, UUID> {
}