package tech.magicbook.analytics.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.magicbook.analytics.api.entity.Application;

import java.util.Optional;
import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<Application, UUID>{
    
    Optional<Application> findByName(String name);
}
