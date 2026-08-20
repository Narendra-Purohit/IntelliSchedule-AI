package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.Subject;
import com.intellischedule.timetable_service.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    // CREATE
    public Subject createSubject(Subject subject) {

        if (subjectRepository.existsByCode(subject.getCode())) {
            throw new IllegalArgumentException(
                    "Subject code already exists: " + subject.getCode()
            );
        }

        validateSubject(subject);

        return subjectRepository.save(subject);
    }

    // GET ALL
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    // GET BY ID
    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Subject not found: " + id)
                );
    }

    // UPDATE
    public Subject updateSubject(Long id, Subject updatedSubject) {

        Subject subject = getSubjectById(id);

        validateSubject(updatedSubject);

        // Check duplicate code
        if (!subject.getCode().equals(updatedSubject.getCode())
                && subjectRepository.existsByCode(updatedSubject.getCode())) {

            throw new IllegalArgumentException(
                    "Subject code already exists: "
                            + updatedSubject.getCode()
            );
        }

        subject.setCode(updatedSubject.getCode());
        subject.setName(updatedSubject.getName());
        subject.setType(updatedSubject.getType());
        subject.setCredits(updatedSubject.getCredits());

        subject.setLectureHoursPerWeek(
                updatedSubject.getLectureHoursPerWeek()
        );

        subject.setTutorialHoursPerWeek(
                updatedSubject.getTutorialHoursPerWeek()
        );

        subject.setPracticalHoursPerWeek(
                updatedSubject.getPracticalHoursPerWeek()
        );

        return subjectRepository.save(subject);
    }

    // DELETE
    public void deleteSubject(Long id) {

        Subject subject = getSubjectById(id);

        subjectRepository.delete(subject);
    }

    // VALIDATION
    private void validateSubject(Subject subject) {

        // Credits
        if (subject.getCredits() == null
                || subject.getCredits() <= 0) {

            throw new IllegalArgumentException(
                    "Credits must be greater than 0"
            );
        }

        // Lecture hours
        if (subject.getLectureHoursPerWeek() == null
                || subject.getLectureHoursPerWeek() < 0) {

            throw new IllegalArgumentException(
                    "Lecture hours cannot be negative"
            );
        }

        // Tutorial hours
        if (subject.getTutorialHoursPerWeek() == null) {
            subject.setTutorialHoursPerWeek(0);
        }

        if (subject.getTutorialHoursPerWeek() < 0) {
            throw new IllegalArgumentException(
                    "Tutorial hours cannot be negative"
            );
        }

        // Practical hours
        if (subject.getPracticalHoursPerWeek() == null) {
            subject.setPracticalHoursPerWeek(0);
        }

        if (subject.getPracticalHoursPerWeek() < 0) {
            throw new IllegalArgumentException(
                    "Practical hours cannot be negative"
            );
        }

        // At least one type of weekly workload
        if (subject.getLectureHoursPerWeek() == 0
                && subject.getTutorialHoursPerWeek() == 0
                && subject.getPracticalHoursPerWeek() == 0) {

            throw new IllegalArgumentException(
                    "At least one weekly lecture, tutorial, "
                            + "or practical hour is required"
            );
        }
    }
}