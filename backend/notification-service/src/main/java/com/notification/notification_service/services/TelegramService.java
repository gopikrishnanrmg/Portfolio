package com.notification.notification_service.services;

import com.notification.notification_service.dtos.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

@Service
public class TelegramService {


    private final String botToken;

    private final String chatId;

    private final String salt;

    private final RestTemplate restTemplate;

    public TelegramService(@Value("${telegram.bot-token}") String botToken,
                           @Value("${telegram.chat-id}") String chatId,
                           @Value("${security.session-salt}") String salt,
                           RestTemplate restTemplate) {
        this.botToken = botToken;
        this.chatId = chatId;
        this.salt = salt;
        this.restTemplate = restTemplate;
    }

    private String formatMessage(Message message) {
        Instant instant = Instant.ofEpochMilli(message.timestamp());
        String time = DateTimeFormatter.ofPattern("MMM dd yyyy • HH:mm:ss")
                .withZone(ZoneId.of("UTC"))
                .format(instant);

        return """
                <b>%s</b>
                
                %s
                
                <b>📨 Contact:</b> %s
                <b>🕒 Sent at:</b> <i>%s</i>
                <b>🔐 Session:</b>
                <code>%s</code>
                """.formatted(message.title(), message.message(), message.contact(), time, md5Hex(message.sessionId() + salt)
        );

    }

    public void sendMessage(Message message) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        Map<String, String> body = new HashMap<>();
        body.put("chat_id", chatId);
        body.put("text", formatMessage(message));
        body.put("parse_mode", "HTML");
        restTemplate.postForObject(url, body, String.class);
    }
}
