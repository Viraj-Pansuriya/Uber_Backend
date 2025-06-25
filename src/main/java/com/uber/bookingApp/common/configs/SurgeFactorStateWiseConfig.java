package com.uber.bookingApp.common.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "surge.factor")
@PropertySource("classpath:surge-factor-state-wise.properties")
@Getter
@Setter
public class SurgeFactorStateWiseConfig {
    List<State> stateList = new ArrayList<>();
}

