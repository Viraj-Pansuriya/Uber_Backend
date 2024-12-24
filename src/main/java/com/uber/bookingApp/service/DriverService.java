package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RiderDto;

import java.util.List;

public interface DriverService {

    RiderDto acceptRide(Long riderId);
    RideDto cancelRide(Long rideId);
    RideDto startRide(Long rideId);
    RideDto endRide(Long rideId);

    RideDto rateRider(Long rideId, Double rating);

    DriverDto getDriverProfile();

    List<RideDto> getAllRides();
}
