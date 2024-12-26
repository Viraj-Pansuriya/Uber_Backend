package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.exceptions.ResourceNotFoundException;
import com.uber.bookingApp.model.*;
import com.uber.bookingApp.model.enums.PaymentMethod;
import com.uber.bookingApp.model.enums.PaymentStatus;
import com.uber.bookingApp.model.enums.RideStatus;
import com.uber.bookingApp.repository.RideRequestRepository;
import com.uber.bookingApp.repository.RiderRepository;
import com.uber.bookingApp.service.*;
import com.uber.bookingApp.strategies.RideStrategyManager;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.uber.bookingApp.model.enums.RideRequestStatus.PENDING;
import static com.uber.bookingApp.model.enums.RideStatus.ACCEPTED;
import static com.uber.bookingApp.model.enums.RideStatus.ONGOING;

@Service
@Slf4j
public class RiderServiceImpl implements RiderService {

    private final DriverService driverService;
    private final PaymentService paymentService;
    private final RideService rideService;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;
    private final RideRequestRepository rideRequestRepository;
    private final RideStrategyManager rideStrategyManager;
    private final RatingService ratingService;

    public RiderServiceImpl(DriverService driverService, PaymentService paymentService, RideService rideService, RiderRepository riderRepository,
                            ModelMapper modelMapper,
                            RideRequestRepository rideRequestRepository,
                            RideStrategyManager rideStrategyManager, RatingService ratingService) {
        this.driverService = driverService;
        this.paymentService = paymentService;
        this.rideService = rideService;
        this.riderRepository = riderRepository;
        this.modelMapper = modelMapper;
        this.rideRequestRepository = rideRequestRepository;
        this.rideStrategyManager = rideStrategyManager;
        this.ratingService = ratingService;
    }

    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {

        Rider rider = getCurrentRider();
        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRequestStatus(PENDING);
        rideRequest.setRider(rider);


        Long fare = (long) Math.ceil(rideStrategyManager.getRideFairCalculationStrategy().calculateFair(rideRequest));
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

        if(!ride.getRideStatus().equals(ACCEPTED)) {
            throw new RuntimeException("Ride cannot be cancelled, invalid status: "+ride.getRideStatus());
        }

        Ride savedRide = rideService.updateRideStatus(ride, RideStatus.REJECTED);
        driverService.updateDriverAvailability(ride.getDriver(), true);

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();

        if(!ride.getRider().equals(rider)) {
            throw new RuntimeException("Rider cannot rate the driver");
        }

        return ratingService.rateDriver(ride , rating);

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

    @Override
    public RideDto initializePayment(Long rideId) {

        // TODO : validation for current rider
        Ride ride = rideService.getRideById(rideId);


        if(!ride.getRideStatus().equals(ONGOING)){
            throw new RuntimeException("Payment can not be initialized in ride status : "+ride.getRideStatus());
        }

        if(ride.getPayment().getPaymentStatus().equals(PaymentStatus.PENDING)){
            throw new RuntimeException("Payment can not be initialized in payment status : "+ride.getPayment().getPaymentStatus());
        }

        Payment payment = paymentService.processPayment(ride);

        ride.setPayment(payment);
        return modelMapper.map(ride, RideDto.class);


    }

    @Override
    public RideDto changePaymentMethod(Long rideId , PaymentMethod paymentMethod) {
        Ride ride = rideService.getRideById(rideId);
        Payment payment = ride.getPayment();

        // TODO : validation for current rider
        if(! (ride.getRideStatus().equals(ONGOING) || ride.getRideStatus().equals(ACCEPTED) )){
            throw new RuntimeException("Payment method can not change in ride status : "+ride.getRideStatus());
        }

        if(!payment.getPaymentStatus().equals(PaymentStatus.PENDING)){
            throw new RuntimeException("Payment method can not change in payment status : "+payment.getPaymentStatus());
        }

        payment =  paymentService.changePaymentMethod(paymentMethod , payment);
        ride.setPayment(payment);
        return modelMapper.map(ride, RideDto.class);
    }
}
