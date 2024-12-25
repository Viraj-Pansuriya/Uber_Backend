package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.exceptions.ResourceNotFoundException;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.User;
import com.uber.bookingApp.model.Wallet;
import com.uber.bookingApp.model.WalletTransaction;
import com.uber.bookingApp.model.enums.TransactionMethod;
import com.uber.bookingApp.model.enums.TransactionType;
import com.uber.bookingApp.repository.WalletRepository;
import com.uber.bookingApp.service.WalletService;
import com.uber.bookingApp.service.WalletTransactionService;
import org.springframework.stereotype.Service;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;

    public WalletServiceImpl(WalletRepository walletRepository, WalletTransactionService walletTransactionService) {
        this.walletRepository = walletRepository;
        this.walletTransactionService = walletTransactionService;
    }

    @Override
    public Wallet createWallet(User user) {
        Wallet wallet =  Wallet.builder()
                .balance(0.0)
                .user(user)
                .build();

        return walletRepository.save(wallet);
    }

    @Override
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {

        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() + amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .ride(ride)
                .wallet(wallet)
                .build();

        walletTransactionService.createWalletTransaction(walletTransaction);
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {


        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() - amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .ride(ride)
                .wallet(wallet)
                .build();

        walletTransactionService.createWalletTransaction(walletTransaction);
        return walletRepository.save(wallet);

    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user with id: "+user.getId()));
    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: "+walletId));
    }
}
