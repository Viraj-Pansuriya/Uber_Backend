package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.User;
import com.uber.bookingApp.repository.RideRequestRepository;
import com.uber.bookingApp.repository.RiderRepository;
import com.uber.bookingApp.service.RiderService;
import com.uber.bookingApp.strategies.RideFairCalculationStrategy;
import com.uber.bookingApp.strategies.impl.DriverMatchingNearestDriverStrategy;
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
    private final RideFairCalculationStrategy rideFairCalculationStrategy;
    private final DriverMatchingNearestDriverStrategy driverMatchingNearestDriverStrategy;
    private final RideRequestRepository rideRequestRepository;
    public RiderServiceImpl(RiderRepository riderRepository, ModelMapper modelMapper, RideFairCalculationStrategy rideFairCalculationStrategy, DriverMatchingNearestDriverStrategy driverMatchingNearestDriverStrategy, RideRequestRepository rideRequestRepository) {
        this.riderRepository = riderRepository;
        this.modelMapper = modelMapper;
        this.rideFairCalculationStrategy = rideFairCalculationStrategy;
        this.driverMatchingNearestDriverStrategy = driverMatchingNearestDriverStrategy;
        this.rideRequestRepository = rideRequestRepository;
    }

    @Override
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {

        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRequestStatus(PENDING);

        Double fare = rideFairCalculationStrategy.calculateFair(rideRequest);
        rideRequest.setFare(fare);

        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        driverMatchingNearestDriverStrategy.findMatchingDrivers(rideRequest);

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
    public DriverDto getRiderProfile() {
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
}
