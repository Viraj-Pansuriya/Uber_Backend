package com.uber.bookingApp.common.configs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailTemplateConfig {

    private Map<String, EventTemplate> templates;

    @Data
    public static class EventTemplate {
        private String subject;
        private String htmlContent;
    }
}
