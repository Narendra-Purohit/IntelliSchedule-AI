package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.Faculty;
import com.intellischedule.timetable_service.entity.FacultyAllocation;
import com.intellischedule.timetable_service.entity.SubjectAllocation;
import com.intellischedule.timetable_service.repository.FacultyAllocationRepository;
import com.intellischedule.timetable_service.repository.FacultyRepository;
import com.intellischedule.timetable_service.repository.SubjectAllocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FacultyAllocationService {

    private final FacultyAllocationRepository facultyAllocationRepository;
    private final FacultyRepository facultyRepository;
    private final SubjectAllocationRepository subjectAllocationRepository;

    public FacultyAllocationService(
            FacultyAllocationRepository facultyAllocationRepository,
            FacultyRepository facultyRepository,
            SubjectAllocationRepository subjectAllocationRepository
    ) {
        this.facultyAllocationRepository = facultyAllocationRepository;
        this.facultyRepository = facultyRepository;
        this.subjectAllocationRepository = subjectAllocationRepository;
    }

    // CREATE
    public FacultyAllocation createAllocation(
            Long facultyId,
            Long subjectAllocationId
    ) {

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Faculty not found: " + facultyId
                        )
                );

        SubjectAllocation subjectAllocation =
                subjectAllocationRepository.findById(subjectAllocationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subject allocation not found: "
                                                + subjectAllocationId
                                )
                        );

        if (facultyAllocationRepository
                .existsByFacultyIdAndSubjectAllocationId(
                        facultyId,
                        subjectAllocationId
                )) {

            throw new IllegalArgumentException(
                    "Faculty is already assigned to this subject allocation"
            );
        }

        FacultyAllocation allocation = new FacultyAllocation();

        allocation.setFaculty(faculty);
        allocation.setSubjectAllocation(subjectAllocation);

        return facultyAllocationRepository.save(allocation);
    }

    // GET ALL
    public List<FacultyAllocation> getAllAllocations() {
        return facultyAllocationRepository.findAll();
    }

    // GET BY ID
    public FacultyAllocation getAllocationById(Long id) {

        return facultyAllocationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Faculty allocation not found: " + id
                        )
                );
    }

    // GET BY FACULTY
    public List<FacultyAllocation> getAllocationsByFaculty(
            Long facultyId
    ) {

        facultyRepository.findById(facultyId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Faculty not found: " + facultyId
                        )
                );

        return facultyAllocationRepository
                .findByFacultyId(facultyId);
    }

    // GET BY SECTION
    public List<FacultyAllocation> getAllocationsBySection(
            Long sectionId
    ) {

        return facultyAllocationRepository
                .findBySubjectAllocationSectionId(sectionId);
    }

    // DELETE
    public void deleteAllocation(Long id) {

        FacultyAllocation allocation =
                getAllocationById(id);

        facultyAllocationRepository.delete(allocation);
    }
}