package com.uber.bookingApp.service;

import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.enums.PaymentMethod;
import com.uber.bookingApp.model.enums.PaymentStatus;

public interface PaymentService {

    Payment createPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);
    Payment processPayment(Ride ride);

    Payment getPaymentByRide(Ride ride);

    Payment getPaymentByTransactionId(String pLinkId);

    Payment changePaymentMethod(PaymentMethod paymentMethod , Payment payment);
}
