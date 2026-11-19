package com.intellischedule.report_service.service;

import com.intellischedule.report_service.dto.ConflictAuditDTO;
import com.intellischedule.report_service.dto.TimetableDTO;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConflictAuditReportGenerator {

    public ConflictAuditDTO auditSchedule(List<TimetableDTO> entries) {
        List<String> conflicts = new ArrayList<>();
        Map<String, String> facultySlotMap = new HashMap<>();
        Map<String, String> roomSlotMap = new HashMap<>();

        for (TimetableDTO e : entries) {
            String facKey = e.dayOfWeek() + ":" + e.timeSlot() + ":" + e.facultyName();
            if (facultySlotMap.containsKey(facKey)) {
                conflicts.add("Faculty conflict: " + e.facultyName() + " double-booked at " + e.dayOfWeek() + " " + e.timeSlot());
            } else {
                facultySlotMap.put(facKey, e.subjectName());
            }

            String roomKey = e.dayOfWeek() + ":" + e.timeSlot() + ":" + e.roomNumber();
            if (roomSlotMap.containsKey(roomKey)) {
                conflicts.add("Room conflict: " + e.roomNumber() + " occupied simultaneously at " + e.dayOfWeek() + " " + e.timeSlot());
            } else {
                roomSlotMap.put(roomKey, e.subjectName());
            }
        }

        return new ConflictAuditDTO(
                entries.size(),
                conflicts.size(),
                conflicts.isEmpty(),
                conflicts
        );
    }
}
