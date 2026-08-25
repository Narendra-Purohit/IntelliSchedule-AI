package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.Faculty;
import com.intellischedule.timetable_service.service.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public ResponseEntity<Faculty> createFaculty(
            @RequestBody Faculty faculty
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(facultyService.createFaculty(faculty));
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculty() {
        return ResponseEntity.ok(
                facultyService.getAllFaculty()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFacultyById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                facultyService.getFacultyById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(
            @PathVariable Long id,
            @RequestBody Faculty faculty
    ) {
        return ResponseEntity.ok(
                facultyService.updateFaculty(id, faculty)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(
            @PathVariable Long id
    ) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }
}