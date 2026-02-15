package com.notification.notification_service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Message(@JsonProperty("session_id") String sessionId, String title, String message, String contact, long timestamp) {
}
