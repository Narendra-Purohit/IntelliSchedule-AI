package com.intellischedule.timetable_service.repository;

import com.intellischedule.timetable_service.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
}