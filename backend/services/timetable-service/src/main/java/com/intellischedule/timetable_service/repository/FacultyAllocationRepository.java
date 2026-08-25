package com.intellischedule.timetable_service.repository;

import com.intellischedule.timetable_service.entity.FacultyAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacultyAllocationRepository
        extends JpaRepository<FacultyAllocation, Long> {

    List<FacultyAllocation> findByFacultyId(Long facultyId);

    List<FacultyAllocation> findBySubjectAllocationSectionId(Long sectionId);

    boolean existsByFacultyIdAndSubjectAllocationId(
            Long facultyId,
            Long subjectAllocationId
    );
}