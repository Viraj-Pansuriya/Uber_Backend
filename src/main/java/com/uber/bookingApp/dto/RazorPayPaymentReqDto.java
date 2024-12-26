package com.uber.bookingApp.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RazorPayPaymentReqDto {

    private Long amount;
    private String currency;
    @JsonProperty("expire_by")
    private Long expireBy;  // The "expire_by" field seems like a timestamp in Unix format (seconds).
    private Customer customer;
    private Notify notify;
    @JsonProperty("reminder_enable")
    private Boolean reminderEnable;  // "reminder_enable" field

    // Nested DTO for Customer
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Customer {
        private String name;
        private String email;
    }

    // Nested DTO for Notify
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Notify {
        private Boolean email = true;
    }
}
