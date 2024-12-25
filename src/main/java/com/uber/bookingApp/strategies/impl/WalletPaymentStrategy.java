package com.uber.bookingApp.strategies.impl;

import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.enums.TransactionMethod;
import com.uber.bookingApp.service.PaymentService;
import com.uber.bookingApp.service.WalletService;
import com.uber.bookingApp.strategies.PaymentStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.uber.bookingApp.model.enums.PaymentStatus.CONFIRMED;

@Service
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentService paymentService;

    public WalletPaymentStrategy(WalletService walletService,
                                 PaymentService paymentService) {
        this.walletService = walletService;
        this.paymentService = paymentService;
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

        paymentService.updatePaymentStatus(payment , CONFIRMED);

    }
}
