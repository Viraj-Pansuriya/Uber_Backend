package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.model.Ride;

public interface RatingService {
    DriverDto rateDriver(Ride ride, Integer rating);

    void createNewRating(Ride updatedRide);

    RiderDto rateRider(Ride ride, Integer rating);
}
