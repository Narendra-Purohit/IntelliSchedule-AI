package com.intellischedule.report_service.controller;

import com.intellischedule.report_service.client.TimetableServiceClient;
import com.intellischedule.report_service.dto.TimetableDTO;
import com.intellischedule.report_service.pdf.FacultySchedulePdfGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports/faculty")
public class FacultyReportController {

    private final TimetableServiceClient timetableClient;
    private final FacultySchedulePdfGenerator facultyPdfGenerator;

    public FacultyReportController(
            TimetableServiceClient timetableClient,
            FacultySchedulePdfGenerator facultyPdfGenerator
    ) {
        this.timetableClient = timetableClient;
        this.facultyPdfGenerator = facultyPdfGenerator;
    }

    @GetMapping("/{facultyName}/pdf")
    public ResponseEntity<byte[]> downloadFacultySchedule(@PathVariable String facultyName) {
        List<TimetableDTO> all = timetableClient.fetchTimetableEntries();
        List<TimetableDTO> filtered = all.stream()
                .filter(t -> facultyName.equalsIgnoreCase(t.facultyName()))
                .toList();

        byte[] pdf = facultyPdfGenerator.generateFacultyPdf(facultyName, filtered);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=schedule_" + facultyName + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
