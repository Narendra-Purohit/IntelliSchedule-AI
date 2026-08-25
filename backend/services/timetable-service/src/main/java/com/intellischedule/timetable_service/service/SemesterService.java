package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.Program;
import com.intellischedule.timetable_service.entity.Semester;
import com.intellischedule.timetable_service.repository.ProgramRepository;
import com.intellischedule.timetable_service.repository.SemesterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterService {

    private final SemesterRepository semesterRepository;
    private final ProgramRepository programRepository;

    public SemesterService(
            SemesterRepository semesterRepository,
            ProgramRepository programRepository
    ) {
        this.semesterRepository = semesterRepository;
        this.programRepository = programRepository;
    }

    // CREATE
    public Semester createSemester(
            Long programId,
            Semester semester
    ) {

        Program program = getProgram(programId);

        validateSemester(semester);

        if (semesterRepository.existsByProgramIdAndSemesterNumber(
                programId,
                semester.getSemesterNumber()
        )) {
            throw new IllegalArgumentException(
                    "Semester already exists for this program"
            );
        }

        semester.setProgram(program);

        return semesterRepository.save(semester);
    }

    // GET ALL
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    // GET BY ID
    public Semester getSemesterById(Long id) {

        return semesterRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Semester not found: " + id
                        )
                );
    }

    // GET BY PROGRAM
    public List<Semester> getSemestersByProgram(Long programId) {

        getProgram(programId);

        return semesterRepository.findByProgramId(programId);
    }

    // UPDATE
    public Semester updateSemester(
            Long id,
            Semester updatedSemester
    ) {

        Semester semester = getSemesterById(id);

        validateSemester(updatedSemester);

        semester.setSemesterNumber(
                updatedSemester.getSemesterNumber()
        );

        semester.setName(
                updatedSemester.getName()
        );

        return semesterRepository.save(semester);
    }

    // DELETE
    public void deleteSemester(Long id) {

        Semester semester = getSemesterById(id);

        semesterRepository.delete(semester);
    }

    // FIND PROGRAM
    private Program getProgram(Long programId) {

        return programRepository.findById(programId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Program not found: " + programId
                        )
                );
    }

    // VALIDATION
    private void validateSemester(Semester semester) {

        if (semester.getSemesterNumber() == null
                || semester.getSemesterNumber() <= 0) {

            throw new IllegalArgumentException(
                    "Semester number must be greater than 0"
            );
        }

        if (semester.getName() == null
                || semester.getName().isBlank()) {

            throw new IllegalArgumentException(
                    "Semester name is required"
            );
        }
    }
}