package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.entity.Program;
import com.intellischedule.timetable_service.service.ProgramService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/programs")
public class ProgramController {

    private final ProgramService programService;

    public ProgramController(ProgramService programService) {
        this.programService = programService;
    }

    @PostMapping
    public ResponseEntity<Program> createProgram(
            @RequestBody Program program
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(programService.createProgram(program));
    }

    @GetMapping
    public ResponseEntity<List<Program>> getAllPrograms() {

        return ResponseEntity.ok(
                programService.getAllPrograms()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgramById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                programService.getProgramById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Program> updateProgram(
            @PathVariable Long id,
            @RequestBody Program program
    ) {
        return ResponseEntity.ok(
                programService.updateProgram(id, program)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(
            @PathVariable Long id
    ) {
        programService.deleteProgram(id);

        return ResponseEntity.noContent().build();
    }
}