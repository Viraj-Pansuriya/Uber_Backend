package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.service.DriverService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverServiceImpl implements DriverService {
    @Override
    public RiderDto acceptRide(Long riderId) {
        return null;
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto startRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto endRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto rateRider(Long rideId, Double rating) {
        return null;
    }

    @Override
    public DriverDto getDriverProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllRides() {
        return List.of();
    }
}
