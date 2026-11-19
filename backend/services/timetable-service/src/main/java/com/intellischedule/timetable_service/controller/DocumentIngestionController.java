package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.dto.AcademicDocumentIngestionDTO;
import com.intellischedule.timetable_service.dto.IngestionResultDTO;
import com.intellischedule.timetable_service.service.DocumentIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/academic")
public class DocumentIngestionController {

    private final DocumentIngestionService ingestionService;

    public DocumentIngestionController(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/ingest-document")
    public ResponseEntity<IngestionResultDTO> ingestDocument(
            @RequestBody AcademicDocumentIngestionDTO document,
            @RequestParam(required = false, defaultValue = "upload.pdf") String filename
    ) {
        IngestionResultDTO result = ingestionService.ingestAcademicDocument(document, filename);
        return ResponseEntity.ok(result);
    }
}
