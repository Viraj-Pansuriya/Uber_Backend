package com.uber.bookingApp.strategies.impl;

import com.uber.bookingApp.dto.RazorPayPaymentGenResp;
import com.uber.bookingApp.dto.RazorPayPaymentReqDto;
import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.repository.PaymentRepository;
import com.uber.bookingApp.strategies.PaymentStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

@Service
public class RazorPayPaymentStrategy implements PaymentStrategy {


    @Value("${razorpay.key}")
    private String razorpayKeyId;

    @Value("${razorpay.secret}")
    private String razorpayKeySecret;
    private final PaymentRepository paymentRepository;

    private final String RAZORPAY_API_URL;

    public RazorPayPaymentStrategy(PaymentRepository paymentRepository,
                                   @Value("${razorpay.payment.link.url}") String razorpayApiUrl) {
        this.paymentRepository = paymentRepository;
        RAZORPAY_API_URL = razorpayApiUrl;
    }


    @Override
    public Payment processPayment(Payment payment) {

        RazorPayPaymentGenResp razorpayResp = createPaymentLink(payment);
        payment.setTransactionId(razorpayResp.getId());
        payment.setPaymentUrl(razorpayResp.getShortUrl());
        return paymentRepository.save(payment);
    }

    public RazorPayPaymentGenResp createPaymentLink(Payment payment) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = razorpayKeyId + ":" + razorpayKeySecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        RazorPayPaymentReqDto razorPayPaymentReqDto = generateReqDtoForPaymentLink(payment);
        HttpEntity<RazorPayPaymentReqDto> entity = new HttpEntity<>(razorPayPaymentReqDto, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RazorPayPaymentGenResp> response = restTemplate.exchange(
                RAZORPAY_API_URL,
                HttpMethod.POST,
                entity,
                RazorPayPaymentGenResp.class
        );

        return response.getBody();
    }

    private RazorPayPaymentReqDto generateReqDtoForPaymentLink(Payment payment) {

        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime futureTime = currentTime.plusMinutes(15);
        long epochSeconds = futureTime.toEpochSecond(ZoneOffset.UTC);

        RazorPayPaymentReqDto.Customer customer = RazorPayPaymentReqDto.Customer.
                builder()
                .name(payment.getRide().getRider().getUser().getName())
                .email(payment.getRide().getRider().getUser().getEmail())
                .contact(payment.getRide().getRider().getUser().getContactNumber())
                .build();

        return RazorPayPaymentReqDto.builder()
                .amount(payment.getAmount() * 100)
                .currency("INR")
                .reminderEnable(true)
                .customer(customer)
                .notify(new RazorPayPaymentReqDto.Notify())
                .expireBy(epochSeconds)
                .build();
    }

}
