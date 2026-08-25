package com.intellischedule.timetable_service.repository;

import com.intellischedule.timetable_service.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository
        extends JpaRepository<Timetable, Long> {

    List<Timetable> findBySubjectAllocationSectionId(Long sectionId);

    List<Timetable> findByFacultyAllocationFacultyId(Long facultyId);

    List<Timetable> findByRoomId(Long roomId);

    List<Timetable> findByTimeSlotId(Long timeSlotId);

    boolean existsByFacultyAllocationFacultyIdAndTimeSlotId(
            Long facultyId,
            Long timeSlotId
    );

    boolean existsByRoomIdAndTimeSlotId(
            Long roomId,
            Long timeSlotId
    );

    boolean existsBySubjectAllocationSectionIdAndTimeSlotId(
            Long sectionId,
            Long timeSlotId
    );
}