package com.analytics.analytics_server.services;

import com.analytics.analytics_server.dtos.GeoData;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetAddress;

@Service
public class GeoIPService {

    private final DatabaseReader reader;

    public GeoIPService() throws IOException {
        ClassPathResource resource = new ClassPathResource("GeoLite2-City.mmdb");
        reader = new DatabaseReader.Builder(resource.getInputStream()).build();
    }

    public GeoData lookup(String ip) throws Exception {
        InetAddress address = InetAddress.getByName(ip);
        CityResponse response = reader.city(address);

        return GeoData.builder()
                .country(response.country().name())
                .city(response.city().name())
                .latitude(response.location().latitude())
                .longitude(response.location().longitude())
                .build();
    }
}
