package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.TimeSlot;
import com.intellischedule.timetable_service.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlot createTimeSlot(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }

    public List<TimeSlot> getAllTimeSlots() {
        return timeSlotRepository.findAll();
    }

    public TimeSlot getTimeSlotById(Long id) {
        return timeSlotRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Time slot not found: " + id)
                );
    }

    public TimeSlot updateTimeSlot(Long id, TimeSlot updatedTimeSlot) {

        TimeSlot timeSlot = getTimeSlotById(id);

        timeSlot.setDayOfWeek(updatedTimeSlot.getDayOfWeek());
        timeSlot.setStartTime(updatedTimeSlot.getStartTime());
        timeSlot.setEndTime(updatedTimeSlot.getEndTime());
        timeSlot.setPeriod(updatedTimeSlot.getPeriod());

        return timeSlotRepository.save(timeSlot);
    }

    public void deleteTimeSlot(Long id) {
        TimeSlot timeSlot = getTimeSlotById(id);
        timeSlotRepository.delete(timeSlot);
    }
}