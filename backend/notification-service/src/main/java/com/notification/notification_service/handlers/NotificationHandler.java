package com.notification.notification_service.handlers;

import com.notification.notification_service.dtos.Message;

public interface NotificationHandler {
    NotificationHandler addHandler(NotificationHandler handler);
    void sendMessage(Message message);
}
