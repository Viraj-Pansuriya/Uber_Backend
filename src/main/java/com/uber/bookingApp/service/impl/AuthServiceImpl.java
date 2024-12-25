package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.UserDto;
import com.uber.bookingApp.dto.auth.SignUpDto;
import com.uber.bookingApp.exceptions.RuntimeConflictException;
import com.uber.bookingApp.model.User;
import com.uber.bookingApp.repository.UserRepository;
import com.uber.bookingApp.service.AuthService;
import com.uber.bookingApp.service.RiderService;
import com.uber.bookingApp.service.WalletService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.uber.bookingApp.model.enums.Role.RIDER;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RiderService riderService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final WalletService walletService;

    public AuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RiderService riderService, BCryptPasswordEncoder passwordEncoder, WalletService walletService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.riderService = riderService;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
    }

    @Override
    public String login(String username, String password) {
        return "";
    }

    @Override
    @Transactional
    public UserDto signUp(SignUpDto signUpDto) {


        boolean isAlreadyExist = userRepository.findByEmail(signUpDto.getEmail()).isPresent();
        if(isAlreadyExist){
           throw new RuntimeConflictException("User with Email Id " + signUpDto.getEmail() + " already exist");
        }

        User newUser = modelMapper.map(signUpDto, User.class);
        newUser.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        newUser.setRoles(Set.of(RIDER));
        User savedUser = userRepository.save(newUser);
        riderService.createNewRider(savedUser);
        walletService.createWallet(savedUser);
        // TODO add wallet related service here

        return modelMapper.map(savedUser, UserDto.class);

    }

    @Override
    public DriverDto onboardNewDriver(Long userId) {
        return null;
    }
}
