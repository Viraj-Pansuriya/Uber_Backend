package com.uber.bookingApp.strategies.impl;

import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.repository.DriverRepository;
import com.uber.bookingApp.strategies.DriverMatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;

    public DriverMatchingHighestRatedDriverStrategy(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    public List<Driver> findMatchingDrivers(RideRequest rideRequest) {
        return driverRepository.findTenNearbyTopRatedDrivers(rideRequest.getPickupLocation());
    }
}
