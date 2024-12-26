package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {


    RideRequestDto requestRide(RideRequestDto rideRequestDto);
    RideDto cancelRide(Long rideId);

    RideDto rateDriver(Long rideId, Double rating);

    RiderDto getRiderProfile();

    Page<RideDto> getAllRides(PageRequest pageRequest);

    Rider createNewRider(User savedUser);

    Rider getCurrentRider();
}
