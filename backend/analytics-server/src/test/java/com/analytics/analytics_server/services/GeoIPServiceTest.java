package com.analytics.analytics_server.services;

import com.analytics.analytics_server.dtos.GeoData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeoIPServiceTest {

    private GeoIPService service;

    @BeforeEach
    void setUp() throws Exception {
        service = new GeoIPService();
    }

    @Test
    void shouldLookupGeoDataForKnownIP() throws Exception {
        String ip = "8.8.8.8";

        GeoData geo = service.lookup(ip);

        assertThat(geo.country()).isNotBlank();
        assertThat(geo.latitude()).isNotNull();
        assertThat(geo.longitude()).isNotNull();

        assertThat(geo.country()).isEqualTo("United States");
    }
}
