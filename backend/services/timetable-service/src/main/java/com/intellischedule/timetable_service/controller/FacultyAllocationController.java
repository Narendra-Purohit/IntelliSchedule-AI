package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.FacultyAllocation;
import com.intellischedule.timetable_service.service.FacultyAllocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FacultyAllocationController {

    private final FacultyAllocationService facultyAllocationService;

    public FacultyAllocationController(
            FacultyAllocationService facultyAllocationService
    ) {
        this.facultyAllocationService = facultyAllocationService;
    }

    // CREATE
    @PostMapping(
            "/faculty/{facultyId}/subject-allocations/{subjectAllocationId}"
    )
    public ResponseEntity<FacultyAllocation> createAllocation(
            @PathVariable Long facultyId,
            @PathVariable Long subjectAllocationId
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        facultyAllocationService.createAllocation(
                                facultyId,
                                subjectAllocationId
                        )
                );
    }

    // GET ALL
    @GetMapping("/faculty-allocations")
    public ResponseEntity<List<FacultyAllocation>>
    getAllAllocations() {

        return ResponseEntity.ok(
                facultyAllocationService.getAllAllocations()
        );
    }

    // GET BY ID
    @GetMapping("/faculty-allocations/{id}")
    public ResponseEntity<FacultyAllocation>
    getAllocationById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                facultyAllocationService.getAllocationById(id)
        );
    }

    // GET BY FACULTY
    @GetMapping("/faculty/{facultyId}/allocations")
    public ResponseEntity<List<FacultyAllocation>>
    getAllocationsByFaculty(
            @PathVariable Long facultyId
    ) {

        return ResponseEntity.ok(
                facultyAllocationService
                        .getAllocationsByFaculty(facultyId)
        );
    }

    // GET BY SECTION
    @GetMapping("/sections/{sectionId}/faculty-allocations")
    public ResponseEntity<List<FacultyAllocation>>
    getAllocationsBySection(
            @PathVariable Long sectionId
    ) {

        return ResponseEntity.ok(
                facultyAllocationService
                        .getAllocationsBySection(sectionId)
        );
    }

    // DELETE
    @DeleteMapping("/faculty-allocations/{id}")
    public ResponseEntity<Void> deleteAllocation(
            @PathVariable Long id
    ) {

        facultyAllocationService.deleteAllocation(id);

        return ResponseEntity.noContent().build();
    }
}