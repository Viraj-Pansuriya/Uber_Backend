package com.uber.bookingApp.service;

import com.uber.bookingApp.model.Ride;

public interface NotificationService {
    void sendNotification(Ride ride);
}
