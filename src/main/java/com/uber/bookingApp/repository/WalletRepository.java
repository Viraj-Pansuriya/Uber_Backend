package com.uber.bookingApp.repository;

import com.uber.bookingApp.model.User;
import com.uber.bookingApp.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);
}
