package com.intellischedule.timetable_service.repository;

import com.intellischedule.timetable_service.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Optional<Faculty> findByFacultyId(String facultyId);

    boolean existsByFacultyId(String facultyId);
}