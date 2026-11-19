package com.intellischedule.timetable_service.dto;

import java.util.List;

public record ExtractedFacultyDTO(
    String facultyName,
    String email,
    String department,
    Integer maxHoursPerWeek,
    List<String> designatedSubjects
) {}
