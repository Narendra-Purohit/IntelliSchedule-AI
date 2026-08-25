package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.Timetable;
import com.intellischedule.timetable_service.service.TimetableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timetables")
public class TimetableController {

    private final TimetableService timetableService;

    public TimetableController(TimetableService timetableService) {
        this.timetableService = timetableService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<Timetable> createTimetable(
            @RequestParam Long subjectAllocationId,
            @RequestParam Long facultyAllocationId,
            @RequestParam Long roomId,
            @RequestParam Long timeSlotId
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        timetableService.createTimetable(
                                subjectAllocationId,
                                facultyAllocationId,
                                roomId,
                                timeSlotId
                        )
                );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<Timetable>> getAllTimetables() {

        return ResponseEntity.ok(
                timetableService.getAllTimetables()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Timetable> getTimetableById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                timetableService.getTimetableById(id)
        );
    }

    // GET BY SECTION
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<Timetable>> getBySection(
            @PathVariable Long sectionId
    ) {

        return ResponseEntity.ok(
                timetableService.getTimetablesBySection(sectionId)
        );
    }

    // GET BY FACULTY
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<Timetable>> getByFaculty(
            @PathVariable Long facultyId
    ) {

        return ResponseEntity.ok(
                timetableService.getTimetablesByFaculty(facultyId)
        );
    }

    // GET BY ROOM
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Timetable>> getByRoom(
            @PathVariable Long roomId
    ) {

        return ResponseEntity.ok(
                timetableService.getTimetablesByRoom(roomId)
        );
    }

    // GET BY TIME SLOT
    @GetMapping("/time-slot/{timeSlotId}")
    public ResponseEntity<List<Timetable>> getByTimeSlot(
            @PathVariable Long timeSlotId
    ) {

        return ResponseEntity.ok(
                timetableService.getTimetablesByTimeSlot(timeSlotId)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTimetable(
            @PathVariable Long id
    ) {

        timetableService.deleteTimetable(id);

        return ResponseEntity.noContent().build();
    }
}