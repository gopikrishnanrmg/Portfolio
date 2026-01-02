package com.analytics.analytics_server.services;

import com.analytics.analytics_server.dtos.CreateAnalyticsRequest;
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

    public void createAnalytics(CreateAnalyticsRequest analyticsRequest, HttpServletRequest request) {
        Analytics event = analyticsMapper.analyticsRequestToAnalytics(analyticsRequest, request);
        analyticsRepository.save(event);
    }
}
