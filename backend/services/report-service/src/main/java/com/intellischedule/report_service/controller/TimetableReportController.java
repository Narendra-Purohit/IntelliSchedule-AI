package com.intellischedule.report_service.controller;

import com.intellischedule.report_service.cache.ReportBinaryCache;
import com.intellischedule.report_service.dto.ConflictAuditDTO;
import com.intellischedule.report_service.service.ReportGenerationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports/timetable")
public class TimetableReportController {

    private final ReportGenerationService reportService;
    private final ReportBinaryCache cache;

    public TimetableReportController(ReportGenerationService reportService, ReportBinaryCache cache) {
        this.reportService = reportService;
        this.cache = cache;
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> downloadPdf() {
        byte[] pdf = cache.get("timetable_master_pdf");
        if (pdf == null) {
            pdf = reportService.generateTimetablePdfReport();
            cache.put("timetable_master_pdf", pdf);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=timetable.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/excel")
    public ResponseEntity<byte[]> downloadExcel() {
        byte[] excel = cache.get("timetable_master_excel");
        if (excel == null) {
            excel = reportService.generateTimetableExcelReport();
            cache.put("timetable_master_excel", excel);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=timetable.xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excel);
    }

    @GetMapping("/conflicts")
    public ResponseEntity<ConflictAuditDTO> getConflictAudit() {
        return ResponseEntity.ok(reportService.auditTimetableConflicts());
    }
}
