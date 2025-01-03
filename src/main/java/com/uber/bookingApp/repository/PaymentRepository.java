package com.uber.bookingApp.repository;

import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRide(Ride ride);

    Optional<Payment> findByTransactionId(String pLinkId);
}
