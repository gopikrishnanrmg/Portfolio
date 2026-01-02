package com.analytics.analytics_server.mappers;

import com.analytics.analytics_server.dtos.CreateAnalyticsRequest;
import com.analytics.analytics_server.models.Analytics;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AnalyticsMapper {
    public Analytics analyticsRequestToAnalytics(CreateAnalyticsRequest analyticsRequest, HttpServletRequest request) {
        return Analytics.builder()
                .timestamp(Instant.now())
                .visitorId(analyticsRequest.visitorId())
                .sessionId(analyticsRequest.sessionId())
                .eventType(analyticsRequest.eventType())
                .page(analyticsRequest.page())
                .ip(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .eventData(analyticsRequest.eventData())
                .build();
    }
}
