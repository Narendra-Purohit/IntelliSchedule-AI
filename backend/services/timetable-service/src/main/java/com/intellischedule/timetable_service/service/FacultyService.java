package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.Faculty;
import com.intellischedule.timetable_service.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {

        if (facultyRepository.existsByFacultyId(faculty.getFacultyId())) {
            throw new IllegalArgumentException(
                    "Faculty ID already exists: " + faculty.getFacultyId()
            );
        }

        return facultyRepository.save(faculty);
    }

    public List<Faculty> getAllFaculty() {
        return facultyRepository.findAll();
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Faculty not found: " + id)
                );
    }

    public Faculty updateFaculty(Long id, Faculty updatedFaculty) {

        Faculty faculty = getFacultyById(id);

        faculty.setFacultyId(updatedFaculty.getFacultyId());
        faculty.setName(updatedFaculty.getName());
        faculty.setDepartment(updatedFaculty.getDepartment());
        faculty.setAvailableDays(updatedFaculty.getAvailableDays());
        faculty.setUnavailableSlots(updatedFaculty.getUnavailableSlots());

        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(Long id) {
        Faculty faculty = getFacultyById(id);
        facultyRepository.delete(faculty);
    }
}