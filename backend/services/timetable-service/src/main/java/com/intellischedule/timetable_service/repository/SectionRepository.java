package com.intellischedule.timetable_service.repository;

import com.intellischedule.timetable_service.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findBySemesterId(Long semesterId);

    boolean existsBySemesterIdAndSectionName(
            Long semesterId,
            String sectionName
    );
}