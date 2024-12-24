package com.uber.bookingApp.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SIgnUpDto {
    private String name;
    private String email;
    private String password;
}
