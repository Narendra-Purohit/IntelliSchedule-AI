package com.intellischedule.timetable_service.service.impl;

import com.intellischedule.timetable_service.dto.ExtractedFacultyDTO;
import com.intellischedule.timetable_service.entity.Faculty;
import com.intellischedule.timetable_service.repository.FacultyRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FacultyIngestionHelper {

    private final FacultyRepository facultyRepository;

    public FacultyIngestionHelper(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public boolean upsertFaculty(ExtractedFacultyDTO dto) {
        Optional<Faculty> existing = facultyRepository.findByName(dto.facultyName());
        if (existing.isPresent()) {
            Faculty f = existing.get();
            f.setDepartment(dto.department() != null ? dto.department() : "General");
            f.setMaxHoursPerWeek(dto.maxHoursPerWeek() != null ? dto.maxHoursPerWeek() : 16);
            facultyRepository.save(f);
            return false;
        } else {
            Faculty f = new Faculty();
            f.setName(dto.facultyName());
            f.setEmail(dto.email() != null ? dto.email() : dto.facultyName().toLowerCase().replace(" ", ".") + "@college.edu");
            f.setDepartment(dto.department() != null ? dto.department() : "General");
            f.setMaxHoursPerWeek(dto.maxHoursPerWeek() != null ? dto.maxHoursPerWeek() : 16);
            facultyRepository.save(f);
            return true;
        }
    }
}
