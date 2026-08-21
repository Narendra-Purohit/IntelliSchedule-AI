package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.Semester;
import com.intellischedule.timetable_service.service.SemesterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SemesterController {

    private final SemesterService semesterService;

    public SemesterController(SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    // CREATE SEMESTER FOR PROGRAM
    @PostMapping("/programs/{programId}/semesters")
    public ResponseEntity<Semester> createSemester(
            @PathVariable Long programId,
            @RequestBody Semester semester
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        semesterService.createSemester(
                                programId,
                                semester
                        )
                );
    }

    // GET ALL SEMESTERS
    @GetMapping("/semesters")
    public ResponseEntity<List<Semester>> getAllSemesters() {

        return ResponseEntity.ok(
                semesterService.getAllSemesters()
        );
    }

    // GET SEMESTER BY ID
    @GetMapping("/semesters/{id}")
    public ResponseEntity<Semester> getSemesterById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                semesterService.getSemesterById(id)
        );
    }

    // GET SEMESTERS OF A PROGRAM
    @GetMapping("/programs/{programId}/semesters")
    public ResponseEntity<List<Semester>> getSemestersByProgram(
            @PathVariable Long programId
    ) {

        return ResponseEntity.ok(
                semesterService.getSemestersByProgram(programId)
        );
    }

    // UPDATE
    @PutMapping("/semesters/{id}")
    public ResponseEntity<Semester> updateSemester(
            @PathVariable Long id,
            @RequestBody Semester semester
    ) {

        return ResponseEntity.ok(
                semesterService.updateSemester(
                        id,
                        semester
                )
        );
    }

    // DELETE
    @DeleteMapping("/semesters/{id}")
    public ResponseEntity<Void> deleteSemester(
            @PathVariable Long id
    ) {

        semesterService.deleteSemester(id);

        return ResponseEntity.noContent().build();
    }
}