package com.uber.bookingApp.controller;

import com.uber.bookingApp.dto.*;
import com.uber.bookingApp.model.enums.PaymentMethod;
import com.uber.bookingApp.service.RiderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider")
@Secured("ROLE_RIDER")
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

    @PostMapping("/rateDriver")
    public ResponseEntity<DriverDto> rateDriver(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok(riderService.rateDriver(ratingDto.getRideId(), ratingDto.getRating()));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<RiderDto> getMyProfile() {
        return ResponseEntity.ok(riderService.getRiderProfile());
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.DESC, "createdTime", "id"));
        return ResponseEntity.ok(riderService.getAllRides(pageRequest));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(riderService.cancelRide(rideId));
    }


}
