package com.uber.bookingApp.service.impl;

import com.uber.bookingApp.model.Ride;
import com.uber.bookingApp.service.EmailService;
import com.uber.bookingApp.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EmailService emailService;

    public NotificationServiceImpl(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @Override
    public void sendNotification(Ride ride) {

        String riderName = ride.getRider().getUser().getName();
        String appName = "BindaasRide";
        String driverName = ride.getDriver().getUser().getName();
        String driverPhone = ride.getDriver().getUser().getContactNumber();
        String otp = ride.getOtp();

        String subject = "Your Ride Awaits! Confirm with OTP 🚖";

        String htmlContent = "<html><body>"
                + "<p>Hi <b>" + riderName + "</b>,</p>"
                + "<p>Your ride with <b>" + appName + "</b> is confirmed! 🚖</p>"
                + "<p>Here's your ride confirmation:</p>"

                // Driver Info with emojis and bold labels
                + "<p><b>🚗 Driver's Name:</b> " + driverName + "</p>"
                + "<p><b>📞 Driver's Contact:</b> " + driverPhone + "</p>"

                // OTP with emoji
                + "<p><b>🔑 Your OTP for ride confirmation is:</b> <b>" + otp + "</b></p>"

                // Additional message
                + "<p>If you need any assistance, feel free to contact us via the app. 📲</p>"
                + "<p>Safe travels! 🛣️<br>" + appName + " Team</p>"

                + "</body></html>";

        String receiver = ride.getRider().getUser().getEmail();
        try{
            emailService.sendEmail(receiver , subject , htmlContent);
        }
        catch (Exception e){
            log.error("Error sending email: {}", e.getMessage());
        }
    }
}
