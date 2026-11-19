package com.intellischedule.timetable_service.dto;

import java.util.List;

public record IngestionResultDTO(
    boolean success,
    int subjectsImported,
    int subjectsUpdated,
    int facultyImported,
    int facultyUpdated,
    List<String> warnings
) {}
