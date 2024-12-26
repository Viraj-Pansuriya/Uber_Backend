package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.exceptions.ResourceNotFoundException;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.repository.RideRequestRepository;
import com.uber.bookingApp.service.RideRequestService;
import org.springframework.stereotype.Service;

@Service
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    public RideRequestServiceImpl(RideRequestRepository rideRequestRepository) {
        this.rideRequestRepository = rideRequestRepository;
    }

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return null;
    }

    @Override
    public void update(RideRequest rideRequest) {

        rideRequestRepository.findById(rideRequest.getId()).orElseThrow(
                ()-> new ResourceNotFoundException("Ride Request not found with id " + rideRequest.getId()));

        rideRequestRepository.save(rideRequest);


    }
}
