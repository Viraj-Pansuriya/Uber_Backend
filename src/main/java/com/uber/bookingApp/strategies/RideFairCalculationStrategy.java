package com.uber.bookingApp.strategies;

import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.model.RideRequest;

public interface RideFairCalculationStrategy {

    double RIDE_FARE_MULTIPLIER = 10;
    double calculateFair(RideRequest rideRequest);


}
