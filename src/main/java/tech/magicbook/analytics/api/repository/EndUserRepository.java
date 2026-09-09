package tech.magicbook.analytics.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tech.magicbook.analytics.api.entity.EndUser;

public interface EndUserRepository extends JpaRepository<EndUser, UUID>, JpaSpecificationExecutor<EndUser> {

    Optional<EndUser> findByApplicationIdAndExternalId(UUID applicationId, String externalId);

}
