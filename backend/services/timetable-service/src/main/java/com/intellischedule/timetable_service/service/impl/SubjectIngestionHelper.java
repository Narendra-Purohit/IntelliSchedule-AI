package com.intellischedule.timetable_service.service.impl;

import com.intellischedule.timetable_service.dto.ExtractedSubjectDTO;
import com.intellischedule.timetable_service.entity.Subject;
import com.intellischedule.timetable_service.repository.SubjectRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SubjectIngestionHelper {

    private final SubjectRepository subjectRepository;

    public SubjectIngestionHelper(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public boolean upsertSubject(ExtractedSubjectDTO dto) {
        Optional<Subject> existing = subjectRepository.findByCode(dto.subjectCode());
        if (existing.isPresent()) {
            Subject s = existing.get();
            s.setName(dto.subjectName());
            s.setWeeklyLectures(dto.theoryHoursPerWeek() != null ? dto.theoryHoursPerWeek() : 3);
            s.setWeeklyLabs(dto.practicalHoursPerWeek() != null ? dto.practicalHoursPerWeek() : 0);
            subjectRepository.save(s);
            return false; // updated
        } else {
            Subject s = new Subject();
            s.setCode(dto.subjectCode());
            s.setName(dto.subjectName());
            s.setWeeklyLectures(dto.theoryHoursPerWeek() != null ? dto.theoryHoursPerWeek() : 3);
            s.setWeeklyLabs(dto.practicalHoursPerWeek() != null ? dto.practicalHoursPerWeek() : 0);
            subjectRepository.save(s);
            return true; // newly inserted
        }
    }
}
