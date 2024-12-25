package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.User;

import java.util.List;

public interface RiderService {


    RideRequestDto requestRide(RideRequestDto rideRequestDto);
    RideDto cancelRide(Long rideId);

    RideDto rateDriver(Long rideId, Double rating);

    RiderDto getRiderProfile();

    List<RideDto> getAllRides();

    Rider createNewRider(User savedUser);

    Rider getCurrentRider();
}
