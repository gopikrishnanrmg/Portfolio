package com.analytics.analytics_server.services;

import com.analytics.analytics_server.dtos.ParsedUserAgent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserAgentServiceTest {

    private UserAgentService service;

    @BeforeEach
    void setUp() {
        service = new UserAgentService();
    }

    @Test
    void shouldParseUserAgentCorrectly() {
        String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) " +
                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

        ParsedUserAgent parsed = service.parse(ua);

        assertThat(parsed.browser()).isNotBlank();
        assertThat(parsed.os()).isNotBlank();
        assertThat(parsed.device()).isNotBlank();

        assertThat(parsed.browser()).containsIgnoringCase("Chrome");
        assertThat(parsed.os()).containsIgnoringCase("Mac");
    }
}
