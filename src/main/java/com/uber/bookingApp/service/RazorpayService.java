package com.uber.bookingApp.service;

public interface RazorpayService {
    void processWebhook(String resp);
}