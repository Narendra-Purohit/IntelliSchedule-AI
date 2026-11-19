package com.intellischedule.report_service.dto;

import java.io.Serializable;

public record SubjectScheduleDTO(
    Long subjectId,
    String subjectCode,
    String subjectName,
    Integer theoryHours,
    Integer practicalHours,
    Integer credits
) implements Serializable {}
