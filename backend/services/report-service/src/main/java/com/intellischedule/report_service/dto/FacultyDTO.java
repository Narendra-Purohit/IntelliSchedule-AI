package com.intellischedule.report_service.dto;

import java.io.Serializable;

public record FacultyDTO(
    Long id,
    String name,
    String email,
    String department,
    Integer maxWeeklyHours,
    Integer allocatedHours
) implements Serializable {}
