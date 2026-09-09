package tech.magicbook.analytics.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.magicbook.analytics.api.entity.DashboardUser;

public interface DashboardUserRepository extends JpaRepository<DashboardUser, UUID>{
    
    Optional<DashboardUser> findByEmail(String email);
}
