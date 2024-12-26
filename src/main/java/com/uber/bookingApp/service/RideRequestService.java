package com.uber.bookingApp.service;


import com.uber.bookingApp.model.RideRequest;

public interface RideRequestService {

    RideRequest findRideRequestById(Long rideRequestId);

    void update(RideRequest rideRequest);
}
