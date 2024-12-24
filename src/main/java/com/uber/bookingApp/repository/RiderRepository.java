package com.uber.bookingApp.repository;

import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderRepository extends JpaRepository<Ride, Long> {
}
