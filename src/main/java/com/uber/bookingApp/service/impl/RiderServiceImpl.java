package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.exceptions.ResourceNotFoundException;
import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.User;
import com.uber.bookingApp.repository.RideRequestRepository;
import com.uber.bookingApp.repository.RiderRepository;
import com.uber.bookingApp.service.RiderService;
import com.uber.bookingApp.strategies.RideStrategyManager;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.uber.bookingApp.model.enums.RideRequestStatus.PENDING;

@Service
@Slf4j
public class RiderServiceImpl implements RiderService {

    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;
    private final RideRequestRepository rideRequestRepository;
    private final RideStrategyManager rideStrategyManager;
    public RiderServiceImpl(RiderRepository riderRepository,
                            ModelMapper modelMapper,
                            RideRequestRepository rideRequestRepository,
                            RideStrategyManager rideStrategyManager) {
        this.riderRepository = riderRepository;
        this.modelMapper = modelMapper;
        this.rideRequestRepository = rideRequestRepository;
        this.rideStrategyManager = rideStrategyManager;
    }

    @Override
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {

        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRequestStatus(PENDING);

        Double fare = rideStrategyManager.getRideFairCalculationStrategy().calculateFair(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        // TODO : Notify drivers about ride request
        List< Driver > matchedDrivers =  rideStrategyManager.getDriverMatchingStrategy(rider.getRating()).findMatchingDrivers(rideRequest);

        return modelMapper.map(savedRideRequest, RideRequestDto.class);
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
    public RiderDto getRiderProfile() {
        return null;
    }

    @Override
    public List<RideDto> getAllRides() {
        return List.of();
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
