package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideStartDto;
import com.uber.bookingApp.model.Driver;

import java.util.List;

public interface DriverService {

    RideDto acceptRide(Long rideRequestId);
    RideDto cancelRide(Long rideId);
    RideDto startRide(Long rideId , RideStartDto rideStartDto);
    RideDto endRide(Long rideId);

    RideDto rateRider(Long rideId, Double rating);

    DriverDto getDriverProfile();

    List<RideDto> getAllRides();

    void updateDriverAvailability(Driver driver, boolean availability);
}
