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
                .ip(extractClientIp(request))
                .userAgent(request.getHeader("User-Agent"))
                .eventData(analyticsRequest.eventData())
                .build();
    }

    private String extractClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
