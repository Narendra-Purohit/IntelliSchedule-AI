package com.intellischedule.timetable_service.repository;

import com.intellischedule.timetable_service.entity.Program;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    boolean existsByCode(String code);
}