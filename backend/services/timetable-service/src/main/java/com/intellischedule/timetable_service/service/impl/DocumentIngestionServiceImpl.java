package com.intellischedule.timetable_service.service.impl;

import com.intellischedule.timetable_service.dto.*;
import com.intellischedule.timetable_service.entity.IngestionAuditLog;
import com.intellischedule.timetable_service.repository.IngestionAuditLogRepository;
import com.intellischedule.timetable_service.service.DocumentIngestionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentIngestionServiceImpl implements DocumentIngestionService {

    private final SubjectIngestionHelper subjectHelper;
    private final FacultyIngestionHelper facultyHelper;
    private final IngestionAuditLogRepository auditRepository;

    public DocumentIngestionServiceImpl(
            SubjectIngestionHelper subjectHelper,
            FacultyIngestionHelper facultyHelper,
            IngestionAuditLogRepository auditRepository
    ) {
        this.subjectHelper = subjectHelper;
        this.facultyHelper = facultyHelper;
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional
    public IngestionResultDTO ingestAcademicDocument(AcademicDocumentIngestionDTO doc, String sourceFilename) {
        int subNew = 0, subUpd = 0;
        int facNew = 0, facUpd = 0;
        List<String> warnings = new ArrayList<>();

        if (doc.subjects() != null) {
            for (ExtractedSubjectDTO s : doc.subjects()) {
                if (s.subjectCode() == null || s.subjectCode().isBlank()) {
                    warnings.add("Skipped subject with missing code: " + s.subjectName());
                    continue;
                }
                boolean inserted = subjectHelper.upsertSubject(s);
                if (inserted) subNew++; else subUpd++;
            }
        }

        if (doc.facultyList() != null) {
            for (ExtractedFacultyDTO f : doc.facultyList()) {
                if (f.facultyName() == null || f.facultyName().isBlank()) {
                    warnings.add("Skipped faculty with empty name");
                    continue;
                }
                boolean inserted = facultyHelper.upsertFaculty(f);
                if (inserted) facNew++; else facUpd++;
            }
        }

        auditRepository.save(new IngestionAuditLog(
                sourceFilename != null ? sourceFilename : "direct-api-payload",
                doc.program(),
                doc.semester(),
                subNew + subUpd,
                facNew + facUpd
        ));

        return new IngestionResultDTO(true, subNew, subUpd, facNew, facUpd, warnings);
    }
}
