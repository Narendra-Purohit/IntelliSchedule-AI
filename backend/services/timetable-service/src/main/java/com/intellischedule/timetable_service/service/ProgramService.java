package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.Program;
import com.intellischedule.timetable_service.repository.ProgramRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {

    private final ProgramRepository programRepository;

    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    // CREATE
    public Program createProgram(Program program) {

        validateProgram(program);

        if (programRepository.existsByCode(program.getCode())) {
            throw new IllegalArgumentException(
                    "Program code already exists: " + program.getCode()
            );
        }

        return programRepository.save(program);
    }

    // GET ALL
    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }

    // GET BY ID
    public Program getProgramById(Long id) {

        return programRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Program not found: " + id)
                );
    }

    // UPDATE
    public Program updateProgram(Long id, Program updatedProgram) {

        Program program = getProgramById(id);

        validateProgram(updatedProgram);

        if (!program.getCode().equals(updatedProgram.getCode())
                && programRepository.existsByCode(updatedProgram.getCode())) {

            throw new IllegalArgumentException(
                    "Program code already exists: "
                            + updatedProgram.getCode()
            );
        }

        program.setCode(updatedProgram.getCode());
        program.setName(updatedProgram.getName());
        program.setDurationYears(updatedProgram.getDurationYears());

        return programRepository.save(program);
    }

    // DELETE
    public void deleteProgram(Long id) {

        Program program = getProgramById(id);

        programRepository.delete(program);
    }

    // VALIDATION
    private void validateProgram(Program program) {

        if (program.getCode() == null
                || program.getCode().isBlank()) {

            throw new IllegalArgumentException(
                    "Program code is required"
            );
        }

        if (program.getName() == null
                || program.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Program name is required"
            );
        }

        if (program.getDurationYears() == null
                || program.getDurationYears() <= 0) {

            throw new IllegalArgumentException(
                    "Duration must be greater than 0"
            );
        }
    }
}