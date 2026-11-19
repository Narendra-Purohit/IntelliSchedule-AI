package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.dto.AcademicDocumentIngestionDTO;
import com.intellischedule.timetable_service.dto.IngestionResultDTO;

public interface DocumentIngestionService {
    IngestionResultDTO ingestAcademicDocument(AcademicDocumentIngestionDTO document, String sourceFilename);
}
