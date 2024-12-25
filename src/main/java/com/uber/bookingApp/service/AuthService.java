package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.UserDto;
import com.uber.bookingApp.dto.auth.SignUpDto;

public interface AuthService {
    String login(String username, String password);
    UserDto signUp(SignUpDto signUpDto);

    DriverDto onboardNewDriver(Long userId);
}
