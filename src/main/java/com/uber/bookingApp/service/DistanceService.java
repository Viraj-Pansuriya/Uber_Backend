package com.uber.bookingApp.service;

import org.locationtech.jts.geom.Point;

public interface DistanceService {

    double calculateDistanceBetweenTwoPoints(Point src , Point dest);
}
