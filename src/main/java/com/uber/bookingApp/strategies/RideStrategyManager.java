package com.uber.bookingApp.strategies;

import com.uber.bookingApp.strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.uber.bookingApp.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.uber.bookingApp.strategies.impl.RideFairDefaultFairCalculationStrategy;
import com.uber.bookingApp.strategies.impl.RideFairSurgePricingFairCalculationStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
public class RideStrategyManager {

    private final DriverMatchingNearestDriverStrategy driverMatchingNearestDriverStrategy;
    private final DriverMatchingHighestRatedDriverStrategy driverMatchingHighestRatedDriverStrategy;
    private final RideFairDefaultFairCalculationStrategy rideFairDefaultFairCalculationStrategy;
    private final RideFairSurgePricingFairCalculationStrategy rideFairSurgePricingFairCalculationStrategy;

    private final double riderRatingThreshold;


    public RideStrategyManager(DriverMatchingNearestDriverStrategy driverMatchingNearestDriverStrategy,
                               DriverMatchingHighestRatedDriverStrategy driverMatchingHighestRatedDriverStrategy,
                               RideFairDefaultFairCalculationStrategy rideFairDefaultFairCalculationStrategy,
                               RideFairSurgePricingFairCalculationStrategy rideFairSurgePricingFairCalculationStrategy,
                               @Value("${rider.rating.threshold}") double riderRatingThreshold) {
        this.driverMatchingNearestDriverStrategy = driverMatchingNearestDriverStrategy;
        this.driverMatchingHighestRatedDriverStrategy = driverMatchingHighestRatedDriverStrategy;
        this.rideFairDefaultFairCalculationStrategy = rideFairDefaultFairCalculationStrategy;
        this.rideFairSurgePricingFairCalculationStrategy = rideFairSurgePricingFairCalculationStrategy;
        this.riderRatingThreshold = riderRatingThreshold;
    }

    public RideFairCalculationStrategy getRideFairCalculationStrategy() {

        LocalTime surgePricingStartTime = LocalTime.of(19 , 0);
        LocalTime surgePricingEndTime = LocalTime.of(22 , 0);

        if (LocalTime.now().isAfter(surgePricingStartTime) && LocalTime.now().isBefore(surgePricingEndTime)) {
            return rideFairSurgePricingFairCalculationStrategy;
        }
        return rideFairSurgePricingFairCalculationStrategy;

    }


    public DriverMatchingStrategy getDriverMatchingStrategy(double rating) {
        return rating > riderRatingThreshold ?
                driverMatchingHighestRatedDriverStrategy : driverMatchingNearestDriverStrategy;
    }

}
