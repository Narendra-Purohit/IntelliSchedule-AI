package com.intellischedule.timetable_service.dto;

public record ExtractedSubjectDTO(
    String subjectCode,
    String subjectName,
    Integer theoryHoursPerWeek,
    Integer practicalHoursPerWeek,
    Integer credits,
    String preferredRoomType
) {}
