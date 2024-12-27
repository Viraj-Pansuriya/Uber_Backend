package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.TokenResponse;
import com.uber.bookingApp.dto.UserDto;
import com.uber.bookingApp.dto.auth.SignUpDto;

public interface AuthService {
    TokenResponse login(String email, String password);
    UserDto signUp(SignUpDto signUpDto);

    DriverDto onboardNewDriver(Long userId , String vehicleId);

    String refreshToken(String refreshToken);
}
