package com.intellischedule.report_service.dto;

import java.io.Serializable;

public record TimetableDTO(
    Long id,
    String dayOfWeek,
    String timeSlot,
    String subjectName,
    String subjectCode,
    String facultyName,
    String roomNumber,
    String sectionName,
    String semesterName
) implements Serializable {}
