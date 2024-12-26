package com.uber.bookingApp.service;

import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.User;
import com.uber.bookingApp.model.Wallet;
import com.uber.bookingApp.model.enums.TransactionMethod;

public interface WalletService {

    Wallet createWallet(User user);

    Wallet findByUser(User user);

    Wallet findWalletById(Long walletId);

    Wallet addMoneyToWallet(User user, Long amount,
                            String transactionId, Ride ride,
                            TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user, Long amount,
                                 String transactionId, Ride ride,
                                 TransactionMethod transactionMethod);
}
