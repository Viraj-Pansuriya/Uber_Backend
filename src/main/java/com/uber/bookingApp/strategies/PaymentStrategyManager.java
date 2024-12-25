package com.uber.bookingApp.strategies;

import com.uber.bookingApp.model.enums.PaymentMethod;
import com.uber.bookingApp.strategies.impl.CashPaymentStrategy;
import com.uber.bookingApp.strategies.impl.WalletPaymentStrategy;
import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyManager {
    private final CashPaymentStrategy cashPaymentStrategy;;
    private final WalletPaymentStrategy walletPaymentStrategy;

    public PaymentStrategyManager(CashPaymentStrategy cashPaymentStrategy,
                                  WalletPaymentStrategy walletPaymentStrategy) {
        this.cashPaymentStrategy = cashPaymentStrategy;
        this.walletPaymentStrategy = walletPaymentStrategy;
    }


    public PaymentStrategy getPaymentStrategy(PaymentMethod paymentMethod) {

        return switch (paymentMethod) {
            case CASH -> cashPaymentStrategy;
            case WALLET -> walletPaymentStrategy;
        };


    }



}
