package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.enums.RideStatus;
import com.uber.bookingApp.repository.RideRepository;
import com.uber.bookingApp.service.RideRequestService;
import com.uber.bookingApp.service.RideService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.uber.bookingApp.model.enums.RideRequestStatus.ACCEPTED;

@Service
public class RideServiceImpl implements RideService {

    private final ModelMapper modelMapper;
    private final RideRequestService rideRequestService;
    private final RideRepository rideRepository;

    public RideServiceImpl(ModelMapper modelMapper, RideRequestService rideRequestService, RideRepository rideRepository) {
        this.modelMapper = modelMapper;
        this.rideRequestService = rideRequestService;
        this.rideRepository = rideRepository;
    }

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride request not found for id : " + rideId));
    }


    @Override
    public Ride createRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRequestStatus(ACCEPTED);
        Ride ride = modelMapper.map(rideRequest , Ride.class);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
        ride.setId(null);
        rideRequestService.update(rideRequest);
        return rideRepository.save(ride);
    }

    private String generateRandomOTP() {

        Random random = new Random();
        return String.format("%04d", random.nextInt(10000)); // 4 digit OTP

    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider , pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver , pageRequest);
    }
}
