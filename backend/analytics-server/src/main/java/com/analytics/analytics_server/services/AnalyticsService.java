package com.analytics.analytics_server.services;

import com.analytics.analytics_server.dtos.CreateAnalyticsRequest;
import com.analytics.analytics_server.dtos.GeoData;
import com.analytics.analytics_server.dtos.ParsedUserAgent;
import com.analytics.analytics_server.mappers.AnalyticsMapper;
import com.analytics.analytics_server.models.Analytics;
import com.analytics.analytics_server.repositories.AnalyticsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final AnalyticsMapper analyticsMapper;
    private final UserAgentService userAgentService;
    private final GeoIPService geoIPService;

    public void createAnalytics(CreateAnalyticsRequest analyticsRequest, HttpServletRequest request) {
        Analytics event = analyticsMapper.analyticsRequestToAnalytics(analyticsRequest, request);

        ParsedUserAgent ua = userAgentService.parse(event.getUserAgent());
        event.setBrowser(ua.browser());
        event.setOs(ua.os());
        event.setDevice(ua.device());

        try {
            GeoData geo = geoIPService.lookup(event.getIp());
            event.setCountry(geo.country());
            event.setCity(geo.city());
            event.setLatitude(geo.latitude());
            event.setLongitude(geo.longitude());
        } catch (Exception ignored) {}
        analyticsRepository.save(event);
    }
}
