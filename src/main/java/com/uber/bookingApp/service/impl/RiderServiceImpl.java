package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.exceptions.ResourceNotFoundException;
import com.uber.bookingApp.model.*;
import com.uber.bookingApp.model.enums.RideStatus;
import com.uber.bookingApp.repository.RideRequestRepository;
import com.uber.bookingApp.repository.RiderRepository;
import com.uber.bookingApp.service.DriverService;
import com.uber.bookingApp.service.RideService;
import com.uber.bookingApp.service.RiderService;
import com.uber.bookingApp.strategies.RideStrategyManager;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.uber.bookingApp.model.enums.RideRequestStatus.PENDING;

@Service
@Slf4j
public class RiderServiceImpl implements RiderService {

    private final DriverService driverService;
    private final RideService rideService;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;
    private final RideRequestRepository rideRequestRepository;
    private final RideStrategyManager rideStrategyManager;
    public RiderServiceImpl(DriverService driverService, RideService rideService, RiderRepository riderRepository,
                            ModelMapper modelMapper,
                            RideRequestRepository rideRequestRepository,
                            RideStrategyManager rideStrategyManager) {
        this.driverService = driverService;
        this.rideService = rideService;
        this.riderRepository = riderRepository;
        this.modelMapper = modelMapper;
        this.rideRequestRepository = rideRequestRepository;
        this.rideStrategyManager = rideStrategyManager;
    }

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {

        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRequestStatus(PENDING);
        rideRequest.setRider(rider);

        Double fare = rideStrategyManager.getRideFairCalculationStrategy().calculateFair(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        // TODO : Notify drivers about ride request
        List< Driver > matchedDrivers =  rideStrategyManager.getDriverMatchingStrategy(rider.getRating()).findMatchingDrivers(rideRequest);

        return modelMapper.map(savedRideRequest, RideRequestDto.class);
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Rider rider = getCurrentRider();
        Ride ride = rideService.getRideById(rideId);

        if(!rider.equals(ride.getRider())) {
            throw new RuntimeException(("Rider does not own this ride with id: "+rideId));
        }

        if(!ride.getRideStatus().equals(RideStatus.ACCEPTED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }

        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.REJECTED);
        driverService.updateDriverAvailability(ride.getDriver(), true);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public RideDto rateDriver(Long rideId, Double rating) {
        return null;
    }

    @Override
    public RiderDto getRiderProfile() {
        Rider rider = getCurrentRider();
        return modelMapper.map(rider, RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllRides(PageRequest pageRequest) {
        Rider currentDriver = getCurrentRider();
        return rideService.getAllRidesOfRider(currentDriver, pageRequest).map(
                ride-> modelMapper.map(ride, RideDto.class)
        );
    }

    @Override
    public Rider createNewRider(User savedUser) {

        Rider rider = Rider.builder()
                .user(savedUser)
                .Rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {

        return riderRepository.findById(1L).orElseThrow( () ->
                new ResourceNotFoundException("Rider not found with id 1")
        );

        // TODO : have to extract this profile from spring security
    }
}
