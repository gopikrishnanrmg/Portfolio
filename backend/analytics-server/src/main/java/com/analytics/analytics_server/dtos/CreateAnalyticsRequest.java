package com.analytics.analytics_server.dtos;

import java.util.Map;

public record CreateAnalyticsRequest(String visitorId, String sessionId, String eventType, String page, Map<String, Object> eventData) {}
