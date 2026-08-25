package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.Section;
import com.intellischedule.timetable_service.service.SectionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // CREATE SECTION FOR SEMESTER
    @PostMapping("/semesters/{semesterId}/sections")
    public ResponseEntity<Section> createSection(
            @PathVariable Long semesterId,
            @RequestBody Section section
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        sectionService.createSection(
                                semesterId,
                                section
                        )
                );
    }

    // GET ALL
    @GetMapping("/sections")
    public ResponseEntity<List<Section>> getAllSections() {

        return ResponseEntity.ok(
                sectionService.getAllSections()
        );
    }

    // GET BY ID
    @GetMapping("/sections/{id}")
    public ResponseEntity<Section> getSectionById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                sectionService.getSectionById(id)
        );
    }

    // GET BY SEMESTER
    @GetMapping("/semesters/{semesterId}/sections")
    public ResponseEntity<List<Section>> getSectionsBySemester(
            @PathVariable Long semesterId
    ) {

        return ResponseEntity.ok(
                sectionService.getSectionsBySemester(semesterId)
        );
    }

    // UPDATE
    @PutMapping("/sections/{id}")
    public ResponseEntity<Section> updateSection(
            @PathVariable Long id,
            @RequestBody Section section
    ) {

        return ResponseEntity.ok(
                sectionService.updateSection(
                        id,
                        section
                )
        );
    }

    // DELETE
    @DeleteMapping("/sections/{id}")
    public ResponseEntity<Void> deleteSection(
            @PathVariable Long id
    ) {

        sectionService.deleteSection(id);

        return ResponseEntity.noContent().build();
    }
}