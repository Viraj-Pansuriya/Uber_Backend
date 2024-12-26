package com.uber.bookingApp.service;

import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface RideService {

    Ride getRideById(Long rideId);


    Ride createRide(RideRequest rideRequestDto , Driver driver);

    Ride updateRideStatus(Ride ride , RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);

}
