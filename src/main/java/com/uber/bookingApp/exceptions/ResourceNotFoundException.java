package com.uber.bookingApp.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {

    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
