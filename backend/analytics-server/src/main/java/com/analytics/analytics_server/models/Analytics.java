package com.analytics.analytics_server.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("analytics")
public class Analytics {
    @Id
    private String analyticsId;
    private Instant timestamp;
    @Indexed
    private String visitorId;
    private String sessionId;
    private String eventType;
    private String page;
    private String ip;
    private String userAgent;
    private String country;
    private String city;
    private Map<String, Object> eventData;
}
