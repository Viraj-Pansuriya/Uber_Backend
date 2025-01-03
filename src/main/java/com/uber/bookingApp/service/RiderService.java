package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.model.User;
import com.uber.bookingApp.model.enums.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {


    RideRequestDto requestRide(RideRequestDto rideRequestDto);
    RideDto cancelRide(Long rideId);

    DriverDto rateDriver(Long rideId, Integer rating);

    RiderDto getRiderProfile();

    Page<RideDto> getAllRides(PageRequest pageRequest);

    Rider createNewRider(User savedUser);

    Rider getCurrentRider();

    RideDto initializePayment(Long rideId);

    RideDto changePaymentMethod(Long rideId , PaymentMethod paymentMethod);
}
