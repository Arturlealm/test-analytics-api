package tech.magicbook.analytics.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.magicbook.analytics.api.entity.EndUser;

public interface EndUserRepository extends JpaRepository<EndUser, UUID> {

    Optional<EndUser> findByApplicationIdAndExternalId(UUID applicationId, String externalId);

}
