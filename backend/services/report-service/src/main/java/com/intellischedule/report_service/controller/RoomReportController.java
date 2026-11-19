package com.intellischedule.report_service.controller;

import com.intellischedule.report_service.client.TimetableServiceClient;
import com.intellischedule.report_service.dto.TimetableDTO;
import com.intellischedule.report_service.pdf.RoomMatrixPdfGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports/room")
public class RoomReportController {

    private final TimetableServiceClient timetableClient;
    private final RoomMatrixPdfGenerator roomMatrixPdfGenerator;

    public RoomReportController(
            TimetableServiceClient timetableClient,
            RoomMatrixPdfGenerator roomMatrixPdfGenerator
    ) {
        this.timetableClient = timetableClient;
        this.roomMatrixPdfGenerator = roomMatrixPdfGenerator;
    }

    @GetMapping("/{roomNumber}/pdf")
    public ResponseEntity<byte[]> downloadRoomMatrix(@PathVariable String roomNumber) {
        List<TimetableDTO> all = timetableClient.fetchTimetableEntries();
        List<TimetableDTO> filtered = all.stream()
                .filter(t -> roomNumber.equalsIgnoreCase(t.roomNumber()))
                .toList();

        byte[] pdf = roomMatrixPdfGenerator.generateRoomMatrix(roomNumber, filtered);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=room_" + roomNumber + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
