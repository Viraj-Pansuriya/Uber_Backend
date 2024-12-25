package com.uber.bookingApp.strategies.impl;

import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.strategies.RideFairCalculationStrategy;



public class RideFairSurgePricingFairCalculationStrategy implements RideFairCalculationStrategy {
    @Override
    public double calculateFair(RideRequest rideRequest) {
        return 0;
    }
}
