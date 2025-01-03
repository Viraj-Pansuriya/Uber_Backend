package com.uber.bookingApp.dto;

import com.uber.bookingApp.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String name;
    private String email;
    private String contactNumber;
    private Set<Role> roles;

}
