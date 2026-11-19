package com.intellischedule.report_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${services.timetable.url:http://localhost:8083}")
    private String timetableServiceUrl;

    @Bean
    public WebClient timetableWebClient() {
        return WebClient.builder()
                .baseUrl(timetableServiceUrl)
                .build();
    }
}
