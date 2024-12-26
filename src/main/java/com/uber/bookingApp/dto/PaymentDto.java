package com.uber.bookingApp.dto;

import com.uber.bookingApp.model.enums.PaymentMethod;
import com.uber.bookingApp.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private PaymentMethod paymentMethod;
    private Long amount;
    private String transactionId;
    private String paymentUrl;
    private LocalDateTime paymentTime;
    private PaymentStatus paymentStatus;
}
