package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.Section;
import com.intellischedule.timetable_service.entity.Subject;
import com.intellischedule.timetable_service.entity.SubjectAllocation;
import com.intellischedule.timetable_service.repository.SectionRepository;
import com.intellischedule.timetable_service.repository.SubjectAllocationRepository;
import com.intellischedule.timetable_service.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectAllocationService {

    private final SubjectAllocationRepository allocationRepository;
    private final SectionRepository sectionRepository;
    private final SubjectRepository subjectRepository;

    public SubjectAllocationService(
            SubjectAllocationRepository allocationRepository,
            SectionRepository sectionRepository,
            SubjectRepository subjectRepository
    ) {
        this.allocationRepository = allocationRepository;
        this.sectionRepository = sectionRepository;
        this.subjectRepository = subjectRepository;
    }

    // CREATE
    public SubjectAllocation createAllocation(
            Long sectionId,
            Long subjectId
    ) {

        Section section = getSection(sectionId);
        Subject subject = getSubject(subjectId);

        if (allocationRepository.existsBySectionIdAndSubjectId(
                sectionId,
                subjectId
        )) {
            throw new IllegalArgumentException(
                    "Subject is already allocated to this section"
            );
        }

        SubjectAllocation allocation = new SubjectAllocation();

        allocation.setSection(section);
        allocation.setSubject(subject);

        return allocationRepository.save(allocation);
    }

    // GET ALL
    public List<SubjectAllocation> getAllAllocations() {
        return allocationRepository.findAll();
    }

    // GET BY ID
    public SubjectAllocation getAllocationById(Long id) {

        return allocationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Subject allocation not found: " + id
                        )
                );
    }

    // GET BY SECTION
    public List<SubjectAllocation> getAllocationsBySection(
            Long sectionId
    ) {

        getSection(sectionId);

        return allocationRepository.findBySectionId(sectionId);
    }

    // DELETE
    public void deleteAllocation(Long id) {

        SubjectAllocation allocation = getAllocationById(id);

        allocationRepository.delete(allocation);
    }

    private Section getSection(Long sectionId) {

        return sectionRepository.findById(sectionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Section not found: " + sectionId
                        )
                );
    }

    private Subject getSubject(Long subjectId) {

        return subjectRepository.findById(subjectId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Subject not found: " + subjectId
                        )
                );
    }
}