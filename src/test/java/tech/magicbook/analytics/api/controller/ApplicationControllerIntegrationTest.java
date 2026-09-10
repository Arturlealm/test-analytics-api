package tech.magicbook.analytics.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import tech.magicbook.analytics.api.service.ApplicationService;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplicationService applicationService;

    @Test
    void shouldRejectRequestWithoutAuthentication() throws Exception {

        mockMvc.perform(get("/v1/applications")).andExpect(status().isUnauthorized());
    }
}