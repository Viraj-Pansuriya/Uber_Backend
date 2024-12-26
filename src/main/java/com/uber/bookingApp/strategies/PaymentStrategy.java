package com.uber.bookingApp.strategies;

import com.uber.bookingApp.model.Payment;

public interface PaymentStrategy {

    double PLATFORM_COMMISSION = 0.3;

    Payment processPayment(Payment payment);

}
