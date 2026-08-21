package com.intellischedule.timetable_service.repository;

import com.intellischedule.timetable_service.entity.SubjectAllocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectAllocationRepository
        extends JpaRepository<SubjectAllocation, Long> {

    List<SubjectAllocation> findBySectionId(Long sectionId);

    boolean existsBySectionIdAndSubjectId(
            Long sectionId,
            Long subjectId
    );
}