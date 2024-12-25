package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.enums.PaymentStatus;
import com.uber.bookingApp.repository.PaymentRepository;
import com.uber.bookingApp.service.PaymentService;
import com.uber.bookingApp.strategies.PaymentStrategyManager;
import org.springframework.stereotype.Service;

import static com.uber.bookingApp.model.enums.PaymentStatus.PENDING;

@Service
public class PaymentServiceImpl implements PaymentService {


    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentStrategyManager paymentStrategyManager) {
        this.paymentRepository = paymentRepository;
        this.paymentStrategyManager = paymentStrategyManager;
    }

    @Override
    public Payment createPayment(Ride ride) {

        Payment payment = Payment.builder()
                .paymentMethod(ride.getPaymentMethod())
                .paymentStatus(PENDING)
                .ride(ride)
                .amount(ride.getFare())
                .build();

        return paymentRepository.save(payment);
    }

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride).orElseThrow(
                () -> new RuntimeException("Payment not found for ride id: " + ride.getId())
        );

        paymentStrategyManager.getPaymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);
        paymentRepository.save(payment);
    }
}
