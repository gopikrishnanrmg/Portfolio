package com.analytics.analytics_server.integration;

import com.analytics.analytics_server.AnalyticsApplication;
import com.analytics.analytics_server.models.Analytics;
import com.analytics.analytics_server.repositories.AnalyticsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AnalyticsApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnalyticsIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AnalyticsRepository repository;

    static final GenericContainer<?> mongo;

    static {
        mongo = new GenericContainer<>("mongo:6.0")
                .withExposedPorts(27017);
        mongo.start();
    }

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri",
                () -> "mongodb://" + mongo.getHost() + ":" + mongo.getMappedPort(27017) + "/testdb");
    }

    @Test
    void contextLoads() { }

    @Test
    void shouldSaveAnalyticsEvent() throws Exception {
        String json = """
            {
              "visitorId": "v123",
              "sessionId": "s456",
              "eventType": "page_view",
              "page": "/projects",
              "eventData": { "scrollDepth": 90 }
            }
        """;

        mockMvc.perform(post("/api/v1/analytics")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isNoContent());

        assert repository.count() == 1;

        Analytics saved = repository.findAll().get(0);
        assert saved.getVisitorId().equals("v123");
        assert saved.getPage().equals("/projects");
    }
}
