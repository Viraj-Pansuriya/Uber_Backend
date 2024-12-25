package com.uber.bookingApp.strategies.impl;

import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.enums.PaymentStatus;
import com.uber.bookingApp.model.enums.TransactionMethod;
import com.uber.bookingApp.repository.PaymentRepository;
import com.uber.bookingApp.service.PaymentService;
import com.uber.bookingApp.service.WalletService;
import com.uber.bookingApp.strategies.PaymentStrategy;
import org.springframework.stereotype.Service;

import static com.uber.bookingApp.model.enums.PaymentStatus.CONFIRMED;

@Service
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentService paymentService;

    public CashPaymentStrategy(WalletService walletService, PaymentService paymentService) {
        this.walletService = walletService;
        this.paymentService = paymentService;
    }

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        double commission = payment.getAmount() * PLATFORM_COMMISSION;
        walletService.deductMoneyFromWallet(driver.getUser(), commission , null , payment.getRide() , TransactionMethod.RIDE);
        paymentService.updatePaymentStatus(payment , CONFIRMED);
    }
}
