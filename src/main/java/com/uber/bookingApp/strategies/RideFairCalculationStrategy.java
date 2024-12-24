package com.uber.bookingApp.strategies;

import com.uber.bookingApp.dto.RideRequestDto;

public interface RideFairCalculationStrategy {
    double calculateFair(RideRequestDto rideRequestDto);


}
