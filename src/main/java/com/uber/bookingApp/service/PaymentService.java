package com.uber.bookingApp.service;

import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.enums.PaymentStatus;

public interface PaymentService {

    Payment createPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);
    void processPayment(Ride ride);
}
