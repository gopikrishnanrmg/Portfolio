package com.gateway.apigateway.integration;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.ORIGIN;
import static org.springframework.http.HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiGatewayIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static WireMockServer wireMockServer;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(9999);
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @BeforeEach
    void resetWireMock() {
        wireMockServer.resetAll();
    }

    @Test
    void routingToMockService_shouldForwardRequestAndReturnResponse() {
        wireMockServer.stubFor(get(urlEqualTo("/api/v1/test"))
                .willReturn(ok("Hello from mock service")));

        webTestClient.get()
                .uri("/mock/api/v1/test")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Hello from mock service");
    }

    @Test
    void corsPreflightRequest_shouldReturnCorrectHeaders() {
        webTestClient.options()
                .uri("/mock/api/v1/test")
                .header(ORIGIN, "http://localhost:3000")
                .header(ACCESS_CONTROL_REQUEST_METHOD, "GET")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Access-Control-Allow-Origin", "http://localhost:3000")
                .expectHeader().valueEquals("Access-Control-Allow-Credentials", "true");
    }

    @Test
    void downstreamUnavailable_shouldReturnErrorStatus() {
        webTestClient.get()
                .uri("/mock/api/v1/missing")
                .exchange()
                .expectStatus().isEqualTo(404);
    }
}

