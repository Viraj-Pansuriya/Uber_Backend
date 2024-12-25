package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);

    void matchWithDriver(RideRequestDto rideRequestDto);

    Ride createRide(RideRequestDto rideRequestDto , DriverDto driver);

    Ride updateRideStatus(Long rideId , RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Long riderId, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Long driverId, PageRequest pageRequest);
}
