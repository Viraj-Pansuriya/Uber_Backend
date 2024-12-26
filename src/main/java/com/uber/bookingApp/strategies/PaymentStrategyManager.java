package com.uber.bookingApp.strategies;

import com.uber.bookingApp.model.enums.PaymentMethod;
import com.uber.bookingApp.strategies.impl.CashPaymentStrategy;
import com.uber.bookingApp.strategies.impl.RazorPayPaymentStrategy;
import com.uber.bookingApp.strategies.impl.WalletPaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyManager {
    private final CashPaymentStrategy cashPaymentStrategy;;
    private final WalletPaymentStrategy walletPaymentStrategy;
    private final RazorPayPaymentStrategy razorPayPaymentStrategy;

    public PaymentStrategyManager(CashPaymentStrategy cashPaymentStrategy,
                                  WalletPaymentStrategy walletPaymentStrategy, RazorPayPaymentStrategy razorPayPaymentStrategy) {
        this.cashPaymentStrategy = cashPaymentStrategy;
        this.walletPaymentStrategy = walletPaymentStrategy;
        this.razorPayPaymentStrategy = razorPayPaymentStrategy;
    }


    public PaymentStrategy getPaymentStrategy(PaymentMethod paymentMethod) {

        return switch (paymentMethod) {
            case CASH -> cashPaymentStrategy;
            case WALLET -> walletPaymentStrategy;
            case RAZOR_PAY -> razorPayPaymentStrategy;
        };


    }



}
