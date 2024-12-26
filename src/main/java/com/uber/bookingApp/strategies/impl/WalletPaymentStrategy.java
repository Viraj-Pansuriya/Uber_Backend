package com.uber.bookingApp.strategies.impl;

import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.enums.PaymentStatus;
import com.uber.bookingApp.model.enums.TransactionMethod;
import com.uber.bookingApp.repository.PaymentRepository;
import com.uber.bookingApp.service.WalletService;
import com.uber.bookingApp.strategies.PaymentStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    public WalletPaymentStrategy(WalletService walletService, PaymentRepository paymentRepository) {
        this.walletService = walletService;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public void processPayment(Payment payment) {

        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(
                rider.getUser() , payment.getAmount() , null  , payment.getRide() , TransactionMethod.RIDE
        );

        double driverPayment = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(
                driver.getUser() , driverPayment , null , payment.getRide() , TransactionMethod.RIDE
        );

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);

    }
}
