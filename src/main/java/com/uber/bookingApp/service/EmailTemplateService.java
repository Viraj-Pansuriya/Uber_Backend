package com.uber.bookingApp.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.uber.bookingApp.common.configs.EmailTemplateConfig;
import com.uber.bookingApp.model.enums.NotificationType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailTemplateService {

    @Value("classpath:EmailTemplates.json")
    private Resource resource;

    private EmailTemplateConfig emailTemplateConfig;

    @PostConstruct
    public void init() throws IOException {
        loadEmailTemplates();  // Call the method to load the templates at startup
    }

    public EmailTemplateConfig loadEmailTemplates() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        emailTemplateConfig = objectMapper.readValue(resource.getInputStream(), EmailTemplateConfig.class);
        return emailTemplateConfig;
    }

    public String getSubject(String notificationType) {
        return emailTemplateConfig.getTemplates().get(notificationType).getSubject();
    }

    public String getHtmlContent(String notificationType) {
        return emailTemplateConfig.getTemplates().get(notificationType).getHtmlContent();
    }
}
