package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideStartDto;
import com.uber.bookingApp.exceptions.RuntimeConflictException;
import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.model.enums.RideStatus;
import com.uber.bookingApp.repository.DriverRepository;
import com.uber.bookingApp.repository.RideRepository;
import com.uber.bookingApp.repository.RideRequestRepository;
import com.uber.bookingApp.service.DriverService;
import com.uber.bookingApp.service.PaymentService;
import com.uber.bookingApp.service.RideService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.uber.bookingApp.model.enums.RideRequestStatus.PENDING;
import static com.uber.bookingApp.model.enums.RideStatus.ACCEPTED;
import static com.uber.bookingApp.model.enums.RideStatus.ONGOING;

@Service
public class DriverServiceImpl implements DriverService {
    private final RideRequestRepository rideRequestRepository;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    public DriverServiceImpl(RideRequestRepository rideRequestRepository,
                             DriverRepository driverRepository,
                             RideService rideService,
                             ModelMapper modelMapper, PaymentService paymentService) {
        this.rideRequestRepository = rideRequestRepository;
        this.driverRepository = driverRepository;
        this.rideService = rideService;
        this.modelMapper = modelMapper;
        this.paymentService = paymentService;
    }

    @Override
    @Transactional
    public RideDto acceptRide(Long rideRequestId) {

        RideRequest rideRequest = rideRequestRepository.findById(rideRequestId).orElseThrow(
                () -> new RuntimeException("Ride request not found for id : " + rideRequestId));

        if(!rideRequest.getRequestStatus().equals(PENDING)){
            throw new RuntimeConflictException("ride request is not in pending state , cannot accept");
        }

        Driver currentDriver = getCurrentDriver();

        if(!currentDriver.getAvailable()){
            throw new RuntimeConflictException("Driver is not available");
        }
        currentDriver.setAvailable(false);
        Driver savedDriver = driverRepository.save(currentDriver);
        Ride ride = rideService.createRide(rideRequest , savedDriver);
        return modelMapper.map(ride, RideDto.class);
    }

    private Driver getCurrentDriver() {

        // TODO : get profile from spring security

        return driverRepository.findById(2L).orElseThrow(() -> new RuntimeException("Driver not found"));

    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto startRide(Long rideId , RideStartDto rideStartDto) {
        Ride ride = rideService.getRideById(rideId);

        Driver driver = getCurrentDriver();

        if(!ride.getRideStatus().equals(ACCEPTED)){
            throw new RuntimeConflictException("ride request is not in accepted state , cannot start");
        }

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeConflictException("Ride is not assigned to this driver");
        }

        if(!rideStartDto.getOtp().equals(ride.getOtp())){
            throw new RuntimeConflictException("Invalid otp");
        }
        ride.setStartedTime(LocalDateTime.now());
        Ride updatedRide = rideService.updateRideStatus(ride , RideStatus.ONGOING);

        paymentService.createPayment(updatedRide);

        return modelMapper.map(updatedRide, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!ride.getRideStatus().equals(ONGOING)){
            throw new RuntimeException("Ride is not in ONGOING state , can not end it.");
        }

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Ride is not assigned to this driver");
        }
        ride.setEndedTime(LocalDateTime.now());
        Ride updatedRide = rideService.updateRideStatus(ride , RideStatus.ENDED);
        updateDriverAvailability(driver , true);
        paymentService.processPayment(ride);

        // TODO : process payment service,
        return modelMapper.map(updatedRide, RideDto.class);
    }

    @Override
    public void updateDriverAvailability(Driver driver, boolean availability) {
        driver.setAvailable(availability);
        driverRepository.save(driver);
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
