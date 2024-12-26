package com.uber.bookingApp.controller;


import com.uber.bookingApp.service.RazorpayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final RazorpayService razorpayService;

    @PostMapping("razorpay")
    public void processWebhook(@RequestBody String payload) {
        razorpayService.processWebhook(payload);
    }
}