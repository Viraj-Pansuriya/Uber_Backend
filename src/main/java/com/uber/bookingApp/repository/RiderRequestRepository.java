package com.uber.bookingApp.repository;

import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderRequestRepository extends JpaRepository<RideRequest, Long> {
}
