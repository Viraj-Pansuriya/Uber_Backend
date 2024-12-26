package com.uber.bookingApp.model;

import com.uber.bookingApp.model.enums.PaymentMethod;
import com.uber.bookingApp.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @OneToOne(fetch = FetchType.LAZY , cascade = CascadeType.ALL)
    private Ride ride;

    private Long amount;

    private String transactionId;
    private String paymentUrl;

    @CreationTimestamp
    private LocalDateTime paymentTime;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
}

