package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.Timetable;
import com.intellischedule.timetable_service.service.TimetableGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timetables")
public class TimetableGenerationController {

    private final TimetableGenerationService
            timetableGenerationService;

    public TimetableGenerationController(
            TimetableGenerationService timetableGenerationService
    ) {
        this.timetableGenerationService =
                timetableGenerationService;
    }

    @PostMapping("/generate")
    public ResponseEntity<List<Timetable>> generateTimetable() {

        return ResponseEntity.ok(
                timetableGenerationService.generateTimetable()
        );
    }
}