package com.notification.notification_service.handlers;

import com.notification.notification_service.dtos.Message;
import com.notification.notification_service.services.TelegramService;
import org.springframework.stereotype.Component;

@Component
public class NotificationTelegramHandler implements NotificationHandler {

    private NotificationHandler nextHandler;

    private final TelegramService service;

    public NotificationTelegramHandler(TelegramService service) {
        this.service = service;
    }

    @Override
    public NotificationHandler addHandler(NotificationHandler handler) {
        nextHandler = handler;
        return nextHandler;
    }

    @Override
    public void sendMessage(Message message) {
        service.sendMessage(message);

        if (nextHandler!=null)
            nextHandler.sendMessage(message);
    }
}
