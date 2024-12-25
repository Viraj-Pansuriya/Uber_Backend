package com.uber.bookingApp.strategies.impl;

import com.uber.bookingApp.common.configs.State;
import com.uber.bookingApp.common.configs.SurgeFactorStateWiseConfig;
import com.uber.bookingApp.model.RideRequest;
import com.uber.bookingApp.service.DistanceService;
import com.uber.bookingApp.strategies.RideFairCalculationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class RideFairSurgePricingFairCalculationStrategy implements RideFairCalculationStrategy {

    private final DistanceService distanceService;

    private final SurgeFactorStateWiseConfig surgeFactorStateWise;
    public RideFairSurgePricingFairCalculationStrategy(DistanceService distanceService, SurgeFactorStateWiseConfig surgeFactorStateWise) {
        this.distanceService = distanceService;
        this.surgeFactorStateWise = surgeFactorStateWise;
    }

    @Override
    public double calculateFair(RideRequest rideRequest) {
        System.out.println(surgeFactorStateWise);
        Point pickupLocation = rideRequest.getPickupLocation();
        double surgeFactor = getSurgeFactorFromNearestState(pickupLocation.getY(), pickupLocation.getX(), surgeFactorStateWise.getStateList());
        double distance = distanceService.calculateDistanceBetweenTwoPoints(rideRequest.getPickupLocation(), rideRequest.getDropOffLocation());
        return distance * RIDE_FARE_MULTIPLIER * surgeFactor;
    }

    // Haversine formula to calculate the distance between two points in kilometers
    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Returns the distance in kilometers
    }

    public static double getSurgeFactorFromNearestState(double userLat, double userLon, List<State> stateList) {
        double nearestDistance = Double.MAX_VALUE;
        double surgeFactor = 1;
        String nearestState = "";

        for (State state : stateList) {
            double distance = haversine(userLat, userLon, state.getLatitude(), state.getLongitude());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                surgeFactor = state.getSurgeFactor();
                nearestState = state.getName();
            }
        }
        log.info("Nearest State: {}, Surge Factor: {}", nearestState, surgeFactor);
        return surgeFactor;
    }
}
