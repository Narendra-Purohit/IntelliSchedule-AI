package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.dto.AcademicDocumentIngestionDTO;
import com.intellischedule.timetable_service.dto.ExtractedSubjectDTO;
import com.intellischedule.timetable_service.dto.IngestionResultDTO;
import com.intellischedule.timetable_service.repository.IngestionAuditLogRepository;
import com.intellischedule.timetable_service.service.impl.DocumentIngestionServiceImpl;
import com.intellischedule.timetable_service.service.impl.FacultyIngestionHelper;
import com.intellischedule.timetable_service.service.impl.SubjectIngestionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DocumentIngestionServiceTest {

    private SubjectIngestionHelper subjectHelper;
    private FacultyIngestionHelper facultyHelper;
    private IngestionAuditLogRepository auditRepo;
    private DocumentIngestionServiceImpl service;

    @BeforeEach
    void setUp() {
        subjectHelper = mock(SubjectIngestionHelper.class);
        facultyHelper = mock(FacultyIngestionHelper.class);
        auditRepo = mock(IngestionAuditLogRepository.class);
        service = new DocumentIngestionServiceImpl(subjectHelper, facultyHelper, auditRepo);
    }

    @Test
    void shouldSuccessfullyIngestSubjects() {
        when(subjectHelper.upsertSubject(any())).thenReturn(true);

        AcademicDocumentIngestionDTO doc = new AcademicDocumentIngestionDTO(
                "GTU", "2026", "Sem 5", "IT",
                List.of(new ExtractedSubjectDTO("IT501", "Web Tech", 3, 2, 5, "Lab")),
                List.of()
        );

        IngestionResultDTO result = service.ingestAcademicDocument(doc, "syllabus.pdf");
        assertTrue(result.success());
        assertEquals(1, result.subjectsImported());
        assertEquals(0, result.subjectsUpdated());
    }
}
