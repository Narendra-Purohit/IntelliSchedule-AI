package com.intellischedule.report_service.service;

import com.intellischedule.report_service.dto.ConflictAuditDTO;

public interface ReportGenerationService {
    byte[] generateTimetablePdfReport();
    byte[] generateTimetableExcelReport();
    ConflictAuditDTO auditTimetableConflicts();
}
