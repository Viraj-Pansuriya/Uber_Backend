package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.model.WalletTransaction;
import com.uber.bookingApp.repository.WalletTransactionRepository;
import com.uber.bookingApp.service.WalletTransactionService;
import org.springframework.stereotype.Service;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;

    public WalletTransactionServiceImpl(WalletTransactionRepository walletTransactionRepository) {
        this.walletTransactionRepository = walletTransactionRepository;
    }

    @Override
    public WalletTransaction createWalletTransaction(WalletTransaction walletTransaction) {
        return walletTransactionRepository.save(walletTransaction);
    }
}
