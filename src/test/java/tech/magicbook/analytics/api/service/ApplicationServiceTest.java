package tech.magicbook.analytics.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import tech.magicbook.analytics.api.dto.ApplicationResponse;
import tech.magicbook.analytics.api.dto.CreateApplicationRequest;
import tech.magicbook.analytics.api.entity.Application;
import tech.magicbook.analytics.api.exception.ApplicationAlreadyExistsException;
import tech.magicbook.analytics.api.repository.ApplicationRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Test
    void shouldCreateApplication() {

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        CreateApplicationRequest request = new CreateApplicationRequest("Analytics Test",
                "Application used in unit test");

        when(applicationRepository.findByName("Analytics Test")).thenReturn(Optional.empty());

        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationResponse response = applicationService.create(request);

        assertEquals("Analytics Test", response.name());
        assertEquals("Application used in unit test", response.description());

        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    void shouldRejectDuplicatedApplicationName() {

        ApplicationService applicationService = new ApplicationService(applicationRepository);

        Application existingApplication = new Application(null, "Analytics Test", "Existing application", null);
        
        when(applicationRepository.findByName("Analytics Test")).thenReturn(Optional.of(existingApplication));

        CreateApplicationRequest request = new CreateApplicationRequest("Analytics Test", "Duplicated application");

        assertThrows(ApplicationAlreadyExistsException.class, () -> applicationService.create(request));
    }
}