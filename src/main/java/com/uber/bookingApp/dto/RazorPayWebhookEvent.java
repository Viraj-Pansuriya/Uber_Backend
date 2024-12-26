package com.uber.bookingApp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class RazorPayWebhookEvent {

    @JsonProperty("account_id")
    private String accountId;

    private String[] contains;

    @JsonProperty("created_at")
    private long createdAt;

    private String entity;

    private String event;

    private Payload payload;

    @Data
    @NoArgsConstructor
    @ToString
    public static class Payload {

        private Order order;
        private Payment payment;
        @JsonProperty("payment_link")
        private PaymentLink paymentLink;
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class Order {

        private Entity entity;
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class Payment {

        private Entity entity;
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static class PaymentLink {

        private Entity entity;

        @Data
        @NoArgsConstructor
        @ToString
        public static class Customer {

            private String email;

            private String name;
        }

        @Data
        @NoArgsConstructor
        @ToString
        public static class Notify {

            private boolean email;
            private boolean sms;
            private boolean whatsapp;
        }

        @Data
        @NoArgsConstructor
        @ToString
        public static class Reminders {

            private String status;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true) // Ignore unknown properties like "entity"
    @Data
    @NoArgsConstructor
    @ToString
    public static class Entity {

        @JsonProperty("accept_partial")
        private boolean acceptPartial;

        private int amount;
        @JsonProperty("amount_paid")
        private int amountPaid;
        @JsonProperty("cancelled_at")
        private long cancelledAt;

        @JsonProperty("created_at")
        private long createdAt;

        private String currency;

        private PaymentLink.Customer customer;

        private String description;

        @JsonProperty("expire_by")
        private long expireBy;

        @JsonProperty("expired_at")
        private long expiredAt;

        @JsonProperty("first_min_partial_amount")
        private int firstMinPartialAmount;

        private String id;

        private PaymentLink.Notify notify;

        @JsonProperty("order_id")
        private String orderId;

        @JsonProperty("reference_id")
        private String referenceId;

        @JsonProperty("reminder_enable")
        private boolean reminderEnable;

        private PaymentLink.Reminders reminders;

        @JsonProperty("short_url")
        private String shortUrl;

        private String status;

        @JsonProperty("updated_at")
        private long updatedAt;

        @JsonProperty("upi_link")
        private boolean upiLink;

        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("whatsapp_link")
        private boolean whatsappLink;


        private int attempts;

    }
}
