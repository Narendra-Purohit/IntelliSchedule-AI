package com.intellischedule.report_service.client;

import com.intellischedule.report_service.dto.TimetableDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class TimetableServiceClient {

    private final WebClient timetableWebClient;

    public TimetableServiceClient(WebClient timetableWebClient) {
        this.timetableWebClient = timetableWebClient;
    }

    public List<TimetableDTO> fetchTimetableEntries() {
        return timetableWebClient.get()
                .uri("/api/v1/timetables")
                .retrieve()
                .bodyToFlux(TimetableDTO.class)
                .collectList()
                .block();
    }
}
