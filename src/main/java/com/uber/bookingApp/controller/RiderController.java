package com.uber.bookingApp.controller;

import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideRequestDto;
import com.uber.bookingApp.model.enums.PaymentMethod;
import com.uber.bookingApp.service.RiderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider")
public class RiderController {

    private final RiderService riderService;

    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @PostMapping("/requestRide")
    public ResponseEntity<RideRequestDto> requestRide(@RequestBody RideRequestDto rideRequestDto) {
        return ResponseEntity.ok(riderService.requestRide(rideRequestDto));
    }

    @PostMapping("/initializePayment/{rideId}")
    public ResponseEntity<RideDto> initializePayment(@PathVariable Long rideId){
        return ResponseEntity.ok(riderService.initializePayment(rideId));
    }

    @PutMapping("/changePaymentMethod/{rideId}")
    public ResponseEntity<RideDto> changePaymentMethod(@PathVariable Long rideId , @RequestBody PaymentMethod paymentMethod){
        return ResponseEntity.ok(riderService.changePaymentMethod(rideId , paymentMethod));
    }

}
