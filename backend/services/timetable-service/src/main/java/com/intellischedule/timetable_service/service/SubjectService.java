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

    public Subject createSubject(Subject subject) {

        if (subjectRepository.existsByCode(subject.getCode())) {
            throw new IllegalArgumentException(
                    "Subject code already exists: " + subject.getCode()
            );
        }

        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Subject not found: " + id)
                );
    }

    public Subject updateSubject(Long id, Subject updatedSubject) {

        Subject subject = getSubjectById(id);

        subject.setCode(updatedSubject.getCode());
        subject.setName(updatedSubject.getName());
        subject.setType(updatedSubject.getType());
        subject.setCredits(updatedSubject.getCredits());
        subject.setPeriodsPerWeek(updatedSubject.getPeriodsPerWeek());

        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long id) {

        Subject subject = getSubjectById(id);

        subjectRepository.delete(subject);
    }
}