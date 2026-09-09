package tech.magicbook.analytics.api.service;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import tech.magicbook.analytics.api.dto.CreateApplicationRequest;
import tech.magicbook.analytics.api.repository.ApplicationRepository;

import tech.magicbook.analytics.api.dto.ApplicationResponse;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.exception.ApplicationAlreadyExistsException;
import tech.magicbook.analytics.api.exception.ApplicationNotFoundException;
import tech.magicbook.analytics.api.exception.InvalidRequestException;
import tech.magicbook.analytics.api.util.UuidGenerator;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public ApplicationResponse create(CreateApplicationRequest request) {

        if (applicationRepository.findByName(request.name()).isPresent()) {
            throw new ApplicationAlreadyExistsException(request.name());
        }

        Application application = new Application(
                UuidGenerator.generateV7(),
                request.name(),
                request.description(),
                OffsetDateTime.now());

        Application savedApplication = applicationRepository.save(application);

        return new ApplicationResponse(
                savedApplication.getId(),
                savedApplication.getName(),
                savedApplication.getDescription(),
                savedApplication.getCreatedAt());
    }

    public ApplicationResponse findById(UUID id) {

        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException(id));

        return new ApplicationResponse(
                application.getId(),
                application.getName(),
                application.getDescription(),
                application.getCreatedAt());
    }

    public Page<ApplicationResponse> findAll(int page, int limit, String sort, String order) {

        if (page < 1) {
            throw new InvalidRequestException("Page must be greater than or equal to 1");
        }

        if (limit < 1 || limit > 100) {
            throw new InvalidRequestException("Limit must be between 1 and 100");
        }

        Set<String> allowedSortFields = Set.of("id", "name", "createdAt");

        if (!allowedSortFields.contains(sort)) {
            throw new InvalidRequestException("Invalid sort field");
        }

        if (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc")) {
            throw new InvalidRequestException("Order must be asc or desc");
        }

        Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

        return applicationRepository.findAll(pageRequest).map(application -> new ApplicationResponse(
                application.getId(),
                application.getName(),
                application.getDescription(),
                application.getCreatedAt()));
    }

}
