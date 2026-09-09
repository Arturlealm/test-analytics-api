package tech.magicbook.analytics.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tech.magicbook.analytics.api.entity.FeatureEvent;

public interface FeatureEventRepository extends JpaRepository<FeatureEvent, UUID>, JpaSpecificationExecutor<FeatureEvent> {
}
