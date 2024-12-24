package com.uber.bookingApp.model;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

//@Entity
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "rating")
    private Double Rating;

    @Column(name = "available")
    private Boolean available;

    @Column(columnDefinition = "Geometry(Point, 4326)") // 4326 represent earth geography
    private Point currentLocation;

}
