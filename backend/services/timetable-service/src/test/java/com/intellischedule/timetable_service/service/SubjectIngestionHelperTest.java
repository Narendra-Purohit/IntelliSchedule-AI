package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.dto.ExtractedSubjectDTO;
import com.intellischedule.timetable_service.entity.Subject;
import com.intellischedule.timetable_service.repository.SubjectRepository;
import com.intellischedule.timetable_service.service.impl.SubjectIngestionHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectIngestionHelperTest {

    private SubjectRepository subjectRepository;
    private SubjectIngestionHelper helper;

    @BeforeEach
    void setUp() {
        subjectRepository = mock(SubjectRepository.class);
        helper = new SubjectIngestionHelper(subjectRepository);
    }

    @Test
    void shouldInsertNewSubjectWhenNotFound() {
        when(subjectRepository.findByCode("CS101")).thenReturn(Optional.empty());

        ExtractedSubjectDTO dto = new ExtractedSubjectDTO("CS101", "Intro to CS", 3, 0, 3, "Room");
        boolean isNew = helper.upsertSubject(dto);

        assertTrue(isNew);
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }
}
