package com.intellischedule.timetable_service.repository;

import com.intellischedule.timetable_service.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SemesterRepository extends JpaRepository<Semester, Long> {

    List<Semester> findByProgramId(Long programId);

    boolean existsByProgramIdAndSemesterNumber(
            Long programId,
            Integer semesterNumber
    );
}