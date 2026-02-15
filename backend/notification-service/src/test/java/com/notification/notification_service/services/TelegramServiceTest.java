package com.notification.notification_service.services;

import com.notification.notification_service.dtos.Message;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TelegramServiceTest {

    @Test
    @SuppressWarnings("unchecked")
    void sendMessage_mustReturnMD5Sum() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        TelegramService telegramService = new TelegramService("token", "id", "salt", restTemplate);
        Message message = new Message("session_id", "title", "message", "email", 1769962920);
        String expectedHash = md5Hex("session_id" + "salt");
        ArgumentCaptor<Map<String, String>> bodyCaptor = ArgumentCaptor.forClass(Map.class);
        telegramService.sendMessage(message);
        verify(restTemplate).postForObject(eq("https://api.telegram.org/bot" + "token" + "/sendMessage"), bodyCaptor.capture(), eq(String.class));
        Map<String, String> body = bodyCaptor.getValue();
        String text = body.getOrDefault("text", null);
        assertNotNull(text);
        assertTrue(text.contains(expectedHash));
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendMessage_mustReturnFormattedTimeStamp() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        TelegramService telegramService = new TelegramService("token", "id", "salt", restTemplate);
        Message message = new Message("session_id", "title", "message", "email", 0L);
        ArgumentCaptor<Map<String, String>> bodyCaptor = ArgumentCaptor.forClass(Map.class);
        telegramService.sendMessage(message);
        verify(restTemplate).postForObject(eq("https://api.telegram.org/bot" + "token" + "/sendMessage"), bodyCaptor.capture(), eq(String.class));
        Map<String, String> body = bodyCaptor.getValue();
        String text = body.getOrDefault("text", null);
        assertNotNull(text);
        assertTrue(text.contains("Jan 01 1970"));
    }

    @Test
    @SuppressWarnings("unchecked")
    void sendMessage_buildsCorrectRequest() {
        RestTemplate restTemplate = mock(RestTemplate.class);
        TelegramService telegramService = new TelegramService("token", "id", "salt", restTemplate);
        Message message = new Message("session_id", "title", "message", "email", 1769962920L);
        ArgumentCaptor<Map> bodyCaptor = ArgumentCaptor.forClass(Map.class);
        telegramService.sendMessage(message);
        verify(restTemplate).postForObject(eq("https://api.telegram.org/bot" + "token" + "/sendMessage"), bodyCaptor.capture(), eq(String.class));
        Map<String, String> body = (Map<String, String>) bodyCaptor.getValue();
        assertTrue(body.containsKey("chat_id"));
        assertTrue(body.containsKey("parse_mode"));
        assertTrue(body.containsKey("text"));
        assertEquals("id", body.get("chat_id"));
        assertEquals("HTML", body.get("parse_mode"));
        String text = body.get("text");
        assertNotNull(text);
        assertTrue(text.contains("<b>title</b>"));
        assertTrue(text.contains("message"));
        assertTrue(text.contains("<b>📨 Contact:</b> email"));
        assertTrue(text.contains("<b>🕒 Sent at:</b> <i>"));
        assertTrue(text.contains("<b>🔐 Session:</b>"));
        assertTrue(text.contains("<code>"));
        assertTrue(text.contains("</code>"));
        assertTrue(text.contains("<b>"));
        assertTrue(text.contains("</b>"));
        assertTrue(text.contains("<i>"));
        assertTrue(text.contains("</i>"));
    }

}