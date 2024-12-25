package com.uber.bookingApp.common.configs;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class State {

    String name;
    double latitude;
    double longitude;
    double surgeFactor;
}
