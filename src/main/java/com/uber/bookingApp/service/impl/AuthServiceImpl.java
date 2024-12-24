package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.UserDto;
import com.uber.bookingApp.dto.auth.SIgnUpDto;
import com.uber.bookingApp.service.AuthService;

public class AuthServiceImpl implements AuthService {
    @Override
    public String login(String username, String password) {
        return "";
    }

    @Override
    public UserDto sighUp(SIgnUpDto sIgnUpDto) {
        return null;
    }

    @Override
    public DriverDto onboardNewDriver(Long userId) {
        return null;
    }
}
