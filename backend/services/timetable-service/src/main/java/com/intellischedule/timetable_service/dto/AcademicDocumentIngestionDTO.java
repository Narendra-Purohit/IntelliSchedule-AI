package com.intellischedule.timetable_service.dto;

import java.util.List;

public record AcademicDocumentIngestionDTO(
    String institutionName,
    String academicYear,
    String semester,
    String program,
    List<ExtractedSubjectDTO> subjects,
    List<ExtractedFacultyDTO> facultyList
) {}
