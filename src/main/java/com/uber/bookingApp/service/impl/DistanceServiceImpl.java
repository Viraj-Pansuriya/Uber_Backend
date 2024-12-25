package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.service.DistanceService;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
public class DistanceServiceImpl implements DistanceService {
    @Override
    public double calculateDistanceBetweenTwoPoints(Point src, Point dest) {
        return 0;
    }
}
