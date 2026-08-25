package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.TimeSlot;
import com.intellischedule.timetable_service.service.TimeSlotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/time-slots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @PostMapping
    public ResponseEntity<TimeSlot> createTimeSlot(
            @RequestBody TimeSlot timeSlot
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(timeSlotService.createTimeSlot(timeSlot));
    }

    @GetMapping
    public ResponseEntity<List<TimeSlot>> getAllTimeSlots() {
        return ResponseEntity.ok(
                timeSlotService.getAllTimeSlots()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeSlot> getTimeSlotById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                timeSlotService.getTimeSlotById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeSlot> updateTimeSlot(
            @PathVariable Long id,
            @RequestBody TimeSlot timeSlot
    ) {
        return ResponseEntity.ok(
                timeSlotService.updateTimeSlot(id, timeSlot)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimeSlot(
            @PathVariable Long id
    ) {
        timeSlotService.deleteTimeSlot(id);
        return ResponseEntity.noContent().build();
    }
}