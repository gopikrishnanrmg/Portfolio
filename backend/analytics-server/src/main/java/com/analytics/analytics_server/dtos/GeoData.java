package com.analytics.analytics_server.dtos;

import lombok.Builder;

@Builder
public record GeoData(String country, String city, Double latitude, Double longitude) {}
