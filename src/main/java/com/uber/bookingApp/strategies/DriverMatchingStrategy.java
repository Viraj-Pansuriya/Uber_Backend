package com.uber.bookingApp.strategies;

import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.model.Driver;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDrivers(RideRequestDto rideRequestDto);
}
