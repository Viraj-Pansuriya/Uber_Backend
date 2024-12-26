package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.RiderDto;
import com.uber.bookingApp.exceptions.ResourceNotFoundException;
import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.Rating;
import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.Rider;
import com.uber.bookingApp.repository.DriverRepository;
import com.uber.bookingApp.repository.RatingRepository;
import com.uber.bookingApp.repository.RiderRepository;
import com.uber.bookingApp.service.RatingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final ModelMapper modelMapper;
    private final RiderRepository riderRepository;

    public RatingServiceImpl(RatingRepository ratingRepository, DriverRepository driverRepository, ModelMapper modelMapper, RiderRepository riderRepository) {
        this.ratingRepository = ratingRepository;
        this.driverRepository = driverRepository;
        this.modelMapper = modelMapper;
        this.riderRepository = riderRepository;
    }

    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {

        Rating ratingEntity = ratingRepository.findByRide(ride).orElseThrow(
                ()-> new ResourceNotFoundException("Rating not found for this ride")
        );

        Driver driver = ride.getDriver();

        if(ratingEntity.getDriverRating() != null){
            throw new RuntimeException("Driver have been already for this ride");
        }

        ratingEntity.setDriverRating(rating);
        ratingRepository.save(ratingEntity);


        Double driverRating = ratingRepository.findByDriver(driver)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average().orElse(0.0);


        driver.setRating(driverRating);
        Driver updatedDriver = driverRepository.save(driver);

        return modelMapper.map(updatedDriver, DriverDto.class);
    }

    @Override
    public void createNewRating(Ride updatedRide) {
        Rating rating = Rating.builder()
                .rider(updatedRide.getRider())
                .driver(updatedRide.getDriver())
                .ride(updatedRide)
                .build();

        ratingRepository.save(rating);
    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {

        Rating ratingEntity = ratingRepository.findByRide(ride).orElseThrow(
                ()-> new ResourceNotFoundException("Rating not found for this ride")
        );

        Rider rider = ride.getRider();

        if(ratingEntity.getRiderRating() != null){
            throw new RuntimeException("Rider have been already for this ride");
        }

        ratingEntity.setRiderRating(rating);
        ratingRepository.save(ratingEntity);


        Double riderRating = ratingRepository.findByRider(rider)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average().orElse(0.0);


        rider.setRating(riderRating);
        Rider updatedRider = riderRepository.save(rider);

        return modelMapper.map(updatedRider, RiderDto.class);
    }
}
