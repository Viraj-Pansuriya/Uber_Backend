package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.dto.DriverDto;
import com.uber.bookingApp.dto.TokenResponse;
import com.uber.bookingApp.dto.UserDto;
import com.uber.bookingApp.dto.auth.SignUpDto;
import com.uber.bookingApp.exceptions.ResourceNotFoundException;
import com.uber.bookingApp.exceptions.RuntimeConflictException;
import com.uber.bookingApp.model.Driver;
import com.uber.bookingApp.model.User;
import com.uber.bookingApp.repository.UserRepository;
import com.uber.bookingApp.security.JWTService;
import com.uber.bookingApp.service.AuthService;
import com.uber.bookingApp.service.DriverService;
import com.uber.bookingApp.service.RiderService;
import com.uber.bookingApp.service.WalletService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Set;

import static com.uber.bookingApp.model.enums.Role.DRIVER;
import static com.uber.bookingApp.model.enums.Role.RIDER;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RiderService riderService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final WalletService walletService;
    private final DriverService driverService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public AuthServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RiderService riderService, BCryptPasswordEncoder passwordEncoder, WalletService walletService, DriverService driverService, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.riderService = riderService;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
        this.driverService = driverService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public TokenResponse login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        User user = (User) authentication.getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        TokenResponse tokens =  TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        setRefreshTokenInCookie(tokens);

        return tokens;
    }

    private void setRefreshTokenInCookie(TokenResponse tokens) {

        Cookie cookie = new Cookie("token", tokens.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(3600); // 1 hour in seconds
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(requestAttributes != null) {
            HttpServletResponse response = requestAttributes.getResponse();
            if(response!= null)
                response.addCookie(cookie);
        }

    }

    private HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
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
    public DriverDto onboardNewDriver(Long userId , String vehicleId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getRoles().contains(DRIVER)){
            throw new RuntimeConflictException("User is already a driver");
        }


        Driver createDriver = Driver.builder()
                .user(user)
                .rating(0.0)
                .vehicleId(vehicleId)
                .available(true)
                .build();
        user.getRoles().add(DRIVER);
        userRepository.save(user);

        Driver savedDriver = driverService.createNewDriver(createDriver);
        return modelMapper.map(savedDriver, DriverDto.class);
    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found " +
                "with id: "+userId));

        return jwtService.generateAccessToken(user);
    }

}
