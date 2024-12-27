package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.model.enums.NotificationType;
import com.uber.bookingApp.service.EmailService;
import com.uber.bookingApp.service.EmailTemplateService;
import com.uber.bookingApp.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;


    public NotificationServiceImpl(EmailService emailService, EmailTemplateService emailTemplateService) {
        this.emailService = emailService;
        this.emailTemplateService = emailTemplateService;
    }

    @Async
    @Override
    public void sendNotification(Ride ride , NotificationType notificationType) {

        String riderName = ride.getRider().getUser().getName();
        String appName = "BindaasRide";
        String driverName = ride.getDriver().getUser().getName();
        String driverPhone = ride.getDriver().getUser().getContactNumber();
        String otp = ride.getOtp();

        String subject = emailTemplateService.getSubject(notificationType.name());
        String htmlContent = emailTemplateService.getHtmlContent(notificationType.name());

        subject = subject
                .replace("{{driverName}}", driverName);

        htmlContent = htmlContent
                .replace("{{riderName}}", riderName)
                .replace("{{appName}}", appName)
                .replace("{{otp}}" , otp)
                .replace("{{driverName}}", driverName)
                .replace("{{driverPhone}}", driverPhone);

        String receiver = ride.getRider().getUser().getEmail();
        try{
            emailService.sendEmail(receiver , subject , htmlContent);
        }
        catch (Exception e){
            log.error("Error sending email: {}", e.getMessage());
        }
    }
}
