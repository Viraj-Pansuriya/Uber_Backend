package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideStartDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.exceptions.RuntimeConflictException;
import com.uber.bookingApp.model.*;
import com.uber.bookingApp.model.enums.RideStatus;
import com.uber.bookingApp.repository.DriverRepository;
import com.uber.bookingApp.repository.RideRequestRepository;
import com.uber.bookingApp.service.DriverService;
import com.uber.bookingApp.service.PaymentService;
import com.uber.bookingApp.service.RatingService;
import com.uber.bookingApp.service.RideService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.uber.bookingApp.model.enums.PaymentStatus.CONFIRMED;
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
    private final RatingService ratingService;
    public DriverServiceImpl(RideRequestRepository rideRequestRepository,
                             DriverRepository driverRepository,
                             RideService rideService,
                             ModelMapper modelMapper, PaymentService paymentService, RatingService ratingService) {
        this.rideRequestRepository = rideRequestRepository;
        this.driverRepository = driverRepository;
        this.rideService = rideService;
        this.modelMapper = modelMapper;
        this.paymentService = paymentService;
        this.ratingService = ratingService;
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

        Driver savedDriver = updateDriverAvailability(currentDriver, false);
        Ride ride = rideService.createRide(rideRequest , savedDriver);
        return modelMapper.map(ride, RideDto.class);
    }

    private Driver getCurrentDriver() {

        // TODO : get profile from spring security

        return driverRepository.findById(2L).orElseThrow(() -> new RuntimeException("Driver not found"));

    }

    @Override
    public RideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);

        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())) {
            throw new RuntimeException("Driver cannot cancel a ride as he has not accepted it earlier");
        }

        if(!ride.getRideStatus().equals(ACCEPTED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }

        rideService.updateRideStatus(ride, RideStatus.REJECTED);
        updateDriverAvailability(driver, true);

        return modelMapper.map(ride, RideDto.class);
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
        Payment payment = paymentService.createPayment(ride);


        ride.setPayment(payment);
        Ride updatedRide = rideService.updateRideStatus(ride , RideStatus.ONGOING);
        ratingService.createNewRating(updatedRide);
        return modelMapper.map(updatedRide, RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        Payment payment = paymentService.getPaymentByRide(ride);

        if(!ride.getRideStatus().equals(ONGOING)){
            throw new RuntimeException("Ride is not in ONGOING state , can not end it.");
        }

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Ride is not assigned to this driver");
        }
        if(!payment.getPaymentStatus().equals(CONFIRMED)){
            throw new RuntimeException("Payment is not confirmed , can not end ride");
        }
        ride.setEndedTime(LocalDateTime.now());
        Ride updatedRide = rideService.updateRideStatus(ride , RideStatus.ENDED);
        updateDriverAvailability(driver , true);
//        paymentService.processPayment(ride);

        // TODO : process payment service,
        return modelMapper.map(updatedRide, RideDto.class);
    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean availability) {
        driver.setAvailable(availability);
        return  driverRepository.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver createDriver) {
        return driverRepository.save(createDriver);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Rider cannot rate the driver");
        }

        if(!ride.getRideStatus().equals(RideStatus.ENDED)) {
            throw new RuntimeException("Ride status is not Ended hence cannot start rating, status: "+ride.getRideStatus());
        }

        return ratingService.rateRider(ride , rating);
    }

    @Override
    public DriverDto getDriverProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver, DriverDto.class);
    }

    @Override
    public Page<RideDto> getAllRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(currentDriver, pageRequest).map(
                ride-> modelMapper.map(ride, RideDto.class)
        );
    }
}
