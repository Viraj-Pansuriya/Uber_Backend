package com.uber.bookingApp.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uber.bookingApp.dto.RazorPayWebhookEvent;
import com.uber.bookingApp.model.Payment;
import com.uber.bookingApp.service.PaymentService;
import com.uber.bookingApp.service.RazorpayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.uber.bookingApp.model.enums.PaymentStatus.CONFIRMED;

@Service
@Slf4j
public class RazorpayServiceImpl implements RazorpayService {


    private static final String PAYMENT_EVENT = "payment_link.paid";
    private final PaymentService paymentService;
    private static final String PAID = "paid";

    public RazorpayServiceImpl(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void processWebhook(String resp) {

        RazorPayWebhookEvent payload = convertJsonToObject(resp);

        String event = payload.getEvent();
        String status = payload.getPayload().getPaymentLink().getEntity().getStatus();

        if(event.equals(PAYMENT_EVENT) && status.equals(PAID)) {
            String p_link_id =  payload.getPayload().getPaymentLink().getEntity().getId();
            Payment payment = paymentService.getPaymentByTransactionId(p_link_id);
            paymentService.updatePaymentStatus(payment,CONFIRMED);
        }
        log.info("Webhook Payload : {}", payload);
    }

    public RazorPayWebhookEvent convertJsonToObject(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, RazorPayWebhookEvent.class);
        } catch (Exception e) {

            log.info("error while decoding webhook json  : {}", e.getMessage());
            return null;
        }
    }
}
