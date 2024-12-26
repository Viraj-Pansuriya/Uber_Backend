package com.uber.bookingApp.repository;

import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Rating;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findByRide(Ride ride);

    List<Rating> findByDriver(Driver driver);

    List<Rating> findByRider(Rider rider);
}
