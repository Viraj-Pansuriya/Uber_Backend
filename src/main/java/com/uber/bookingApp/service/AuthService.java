package com.uber.bookingApp.service;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.UserDto;
import com.uber.bookingApp.dto.auth.SIgnUpDto;

public interface AuthService {
    String login(String username, String password);
    UserDto sighUp(SIgnUpDto sIgnUpDto);

    DriverDto onboardNewDriver(Long userId);
}
