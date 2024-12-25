package com.uber.bookingApp.strategies.impl;

import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.strategies.DriverMatchingStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverMatchingHighestRatedtDriverStrategy implements DriverMatchingStrategy {
    @Override
    public List<Driver> findMatchingDrivers(RideRequest rideRequest) {
        return List.of();
    }
}
