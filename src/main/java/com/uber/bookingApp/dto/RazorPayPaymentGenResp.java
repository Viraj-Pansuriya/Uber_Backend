package com.uber.bookingApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RazorPayPaymentGenResp {


    @JsonProperty("amount")
    private int amount;

    @JsonProperty("amount_paid")
    private int amountPaid;

    @JsonProperty("cancelled_at")
    private int cancelledAt;

    @JsonProperty("created_at")
    private long createdAt;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("customer")
    private CustomerDTO customer;


    @JsonProperty("expire_by")
    private long expireBy;

    @JsonProperty("expired_at")
    private int expiredAt;

    @JsonProperty("id")
    private String id;

    @JsonProperty("notify")
    private NotifyDTO notify;

    @JsonProperty("payments")
    private Object payments; // Can be List or other appropriate type, depending on actual response.

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("reminder_enable")
    private boolean reminderEnable;

    @JsonProperty("reminders")
    private List<Object> reminders; // Replace with actual type if available

    @JsonProperty("short_url")
    private String shortUrl;

    @JsonProperty("status")
    private String status;

    @JsonProperty("updated_at")
    private long updatedAt;

    @JsonProperty("upi_link")
    private boolean upiLink;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("whatsapp_link")
    private boolean whatsappLink;

    // Inner DTOs for nested structures

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerDTO {

        @JsonProperty("email")
        private String email;

        @JsonProperty("name")
        private String name;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotifyDTO {

        @JsonProperty("email")
        private boolean email;

        @JsonProperty("sms")
        private boolean sms;

        @JsonProperty("whatsapp")
        private boolean whatsapp;
    }
}
