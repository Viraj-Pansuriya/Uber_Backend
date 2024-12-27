package com.uber.bookingApp.controller;


import com.uber.bookingApp.dto.*;
import com.uber.bookingApp.service.DriverService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
@Secured("ROLE_DRIVER")
public class DriverController {


    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }


    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long rideRequestId) {
        return ResponseEntity.ok(driverService.acceptRide(rideRequestId));
    }

    @PostMapping("/startRide/{rideId}")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideId , @RequestBody RideStartDto rideStartDto) {
        return ResponseEntity.ok(driverService.startRide(rideId , rideStartDto));
    }

    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<RideDto> endRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.endRide(rideId));
    }

    @PostMapping("/rateRider")
    public ResponseEntity<RiderDto> rateRider(@RequestBody RatingDto ratingDto) {
        return ResponseEntity.ok(driverService.rateRider(ratingDto.getRideId(), ratingDto.getRating()));

    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDto> getMyProfile() {
        return ResponseEntity.ok(driverService.getDriverProfile());
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<Page<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                       @RequestParam(defaultValue = "10", required = false) Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageOffset, pageSize,
                Sort.by(Sort.Direction.DESC, "createdTime", "id"));
        return ResponseEntity.ok(driverService.getAllRides(pageRequest));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDto> cancelRide(@PathVariable Long rideId) {
        return ResponseEntity.ok(driverService.cancelRide(rideId));
    }
}
