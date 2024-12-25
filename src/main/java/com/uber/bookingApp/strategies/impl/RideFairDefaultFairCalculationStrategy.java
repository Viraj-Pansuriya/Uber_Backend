package com.uber.bookingApp.strategies.impl;

import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.service.DistanceService;
import com.uber.bookingApp.strategies.RideFairCalculationStrategy;
import org.springframework.stereotype.Service;

@Service
public class RideFairDefaultFairCalculationStrategy implements RideFairCalculationStrategy {
    private final DistanceService distanceService;

    public RideFairDefaultFairCalculationStrategy(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    @Override
    public double calculateFair(RideRequest rideRequest) {
        return distanceService.calculateDistanceBetweenTwoPoints
                (rideRequest.getPickupLocation(), rideRequest.getDropOffLocation())
                * RIDE_FARE_MULTIPLIER;
    }
}
