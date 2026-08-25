package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.Subject;
import com.intellischedule.timetable_service.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<Subject> createSubject(
            @RequestBody Subject subject
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subjectService.createSubject(subject));
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return ResponseEntity.ok(
                subjectService.getAllSubjects()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subject> getSubjectById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                subjectService.getSubjectById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(
            @PathVariable Long id,
            @RequestBody Subject subject
    ) {
        return ResponseEntity.ok(
                subjectService.updateSubject(id, subject)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(
            @PathVariable Long id
    ) {
        subjectService.deleteSubject(id);

        return ResponseEntity.noContent().build();
    }
}