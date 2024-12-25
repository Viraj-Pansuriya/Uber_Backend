package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.service.RiderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiderServiceImpl implements RiderService {
    @Override
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        return null;
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto rateDriver(Long rideId, Double rating) {
        return null;
    }

    @Override
    public DriverDto getRiderProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllRides() {
        return List.of();
    }
}
