package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.SubjectAllocation;
import com.intellischedule.timetable_service.service.SubjectAllocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SubjectAllocationController {

    private final SubjectAllocationService allocationService;

    public SubjectAllocationController(
            SubjectAllocationService allocationService
    ) {
        this.allocationService = allocationService;
    }

    // CREATE
    @PostMapping("/sections/{sectionId}/subjects/{subjectId}")
    public ResponseEntity<SubjectAllocation> createAllocation(
            @PathVariable Long sectionId,
            @PathVariable Long subjectId
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        allocationService.createAllocation(
                                sectionId,
                                subjectId
                        )
                );
    }

    // GET ALL
    @GetMapping("/subject-allocations")
    public ResponseEntity<List<SubjectAllocation>> getAllAllocations() {

        return ResponseEntity.ok(
                allocationService.getAllAllocations()
        );
    }

    // GET BY ID
    @GetMapping("/subject-allocations/{id}")
    public ResponseEntity<SubjectAllocation> getAllocationById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                allocationService.getAllocationById(id)
        );
    }

    // GET BY SECTION
    @GetMapping("/sections/{sectionId}/subjects")
    public ResponseEntity<List<SubjectAllocation>>
    getAllocationsBySection(
            @PathVariable Long sectionId
    ) {

        return ResponseEntity.ok(
                allocationService.getAllocationsBySection(sectionId)
        );
    }

    // DELETE
    @DeleteMapping("/subject-allocations/{id}")
    public ResponseEntity<Void> deleteAllocation(
            @PathVariable Long id
    ) {

        allocationService.deleteAllocation(id);

        return ResponseEntity.noContent().build();
    }
}