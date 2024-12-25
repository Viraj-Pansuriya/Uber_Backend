package com.uber.bookingApp.controller;


import com.uber.bookingApp.dto.RideDto;
import com.uber.bookingApp.dto.RideStartDto;
import com.uber.bookingApp.service.DriverService;
import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/driver")
public class DriverController {


    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/acceptRide/{rideRequestId}")
    public RideDto acceptRide(@PathVariable Long rideRequestId) {
        return driverService.acceptRide(rideRequestId);
    }

    @PostMapping("/startRide/{rideId}")
    public RideDto startRide(@PathVariable Long rideId , @RequestBody RideStartDto rideStartDto) {
        return  driverService.startRide(rideId , rideStartDto);
    }
}
