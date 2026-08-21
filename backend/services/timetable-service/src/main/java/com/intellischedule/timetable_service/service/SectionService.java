package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.Section;
import com.intellischedule.timetable_service.entity.Semester;
import com.intellischedule.timetable_service.repository.SectionRepository;
import com.intellischedule.timetable_service.repository.SemesterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final SemesterRepository semesterRepository;

    public SectionService(
            SectionRepository sectionRepository,
            SemesterRepository semesterRepository
    ) {
        this.sectionRepository = sectionRepository;
        this.semesterRepository = semesterRepository;
    }

    // CREATE
    public Section createSection(
            Long semesterId,
            Section section
    ) {

        Semester semester = getSemester(semesterId);

        validateSection(section);

        if (sectionRepository.existsBySemesterIdAndSectionName(
                semesterId,
                section.getSectionName()
        )) {
            throw new IllegalArgumentException(
                    "Section already exists for this semester"
            );
        }

        section.setSemester(semester);

        return sectionRepository.save(section);
    }

    // GET ALL
    public List<Section> getAllSections() {
        return sectionRepository.findAll();
    }

    // GET BY ID
    public Section getSectionById(Long id) {

        return sectionRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Section not found: " + id
                        )
                );
    }

    // GET BY SEMESTER
    public List<Section> getSectionsBySemester(Long semesterId) {

        getSemester(semesterId);

        return sectionRepository.findBySemesterId(semesterId);
    }

    // UPDATE
    public Section updateSection(
            Long id,
            Section updatedSection
    ) {

        Section section = getSectionById(id);

        validateSection(updatedSection);

        section.setSectionName(
                updatedSection.getSectionName()
        );

        section.setStudentCount(
                updatedSection.getStudentCount()
        );

        return sectionRepository.save(section);
    }

    // DELETE
    public void deleteSection(Long id) {

        Section section = getSectionById(id);

        sectionRepository.delete(section);
    }

    // FIND SEMESTER
    private Semester getSemester(Long semesterId) {

        return semesterRepository.findById(semesterId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Semester not found: " + semesterId
                        )
                );
    }

    // VALIDATION
    private void validateSection(Section section) {

        if (section.getSectionName() == null
                || section.getSectionName().isBlank()) {

            throw new IllegalArgumentException(
                    "Section name is required"
            );
        }

        if (section.getStudentCount() == null
                || section.getStudentCount() < 0) {

            throw new IllegalArgumentException(
                    "Student count cannot be negative"
            );
        }
    }
}