package com.uber.bookingApp.service;

import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.enums.NotificationType;

public interface NotificationService {
    void sendNotification(Ride ride , NotificationType notificationType);
}
