package com.uber.bookingApp.dto;

import com.uber.bookingApp.model.enums.PaymentMethod;
import com.uber.bookingApp.model.enums.RideRequestStatus;
import com.uber.bookingApp.model.enums.RideStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideDto {

    private Long id;
    private Point pickupLocation;
    private Point dropOffLocation;
    private LocalDateTime createdTime;
    private RiderDto rider;
    private DriverDto driver;
    private PaymentMethod paymentMethod;
    private RideRequestStatus requestStatus;
    private RideStatus rideStatus;
    private String otp;
    private Double fare;
    private LocalDateTime startedTime;
    private LocalDateTime endedTime;
}
