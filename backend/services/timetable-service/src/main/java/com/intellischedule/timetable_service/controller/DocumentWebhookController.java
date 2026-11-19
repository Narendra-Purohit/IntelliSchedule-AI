package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.dto.AcademicDocumentIngestionDTO;
import com.intellischedule.timetable_service.dto.IngestionResultDTO;
import com.intellischedule.timetable_service.service.DocumentIngestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/webhooks")
public class DocumentWebhookController {

    private final DocumentIngestionService ingestionService;

    public DocumentWebhookController(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }

    @PostMapping("/document-intelligence")
    public ResponseEntity<Map<String, Object>> handleExtractionWebhook(@RequestBody Map<String, Object> payload) {
        String filename = (String) payload.getOrDefault("filename", "webhook-file.pdf");
        return ResponseEntity.ok(Map.of(
                "received", true,
                "filename", filename,
                "status", "PROCESSED"
        ));
    }
}
