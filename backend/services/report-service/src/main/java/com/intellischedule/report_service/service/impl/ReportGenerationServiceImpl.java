package com.intellischedule.report_service.service.impl;

import com.intellischedule.report_service.client.TimetableServiceClient;
import com.intellischedule.report_service.dto.ConflictAuditDTO;
import com.intellischedule.report_service.dto.TimetableDTO;
import com.intellischedule.report_service.excel.TimetableExcelGenerator;
import com.intellischedule.report_service.pdf.TimetablePdfGenerator;
import com.intellischedule.report_service.service.ConflictAuditReportGenerator;
import com.intellischedule.report_service.service.ReportGenerationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportGenerationServiceImpl implements ReportGenerationService {

    private final TimetableServiceClient timetableClient;
    private final TimetablePdfGenerator pdfGenerator;
    private final TimetableExcelGenerator excelGenerator;
    private final ConflictAuditReportGenerator auditGenerator;

    public ReportGenerationServiceImpl(
            TimetableServiceClient timetableClient,
            TimetablePdfGenerator pdfGenerator,
            TimetableExcelGenerator excelGenerator,
            ConflictAuditReportGenerator auditGenerator
    ) {
        this.timetableClient = timetableClient;
        this.pdfGenerator = pdfGenerator;
        this.excelGenerator = excelGenerator;
        this.auditGenerator = auditGenerator;
    }

    @Override
    public byte[] generateTimetablePdfReport() {
        List<TimetableDTO> entries = timetableClient.fetchTimetableEntries();
        return pdfGenerator.generatePdf(entries, "Master Timetable Schedule");
    }

    @Override
    public byte[] generateTimetableExcelReport() {
        List<TimetableDTO> entries = timetableClient.fetchTimetableEntries();
        return excelGenerator.generateExcel(entries);
    }

    @Override
    public ConflictAuditDTO auditTimetableConflicts() {
        List<TimetableDTO> entries = timetableClient.fetchTimetableEntries();
        return auditGenerator.auditSchedule(entries);
    }
}
