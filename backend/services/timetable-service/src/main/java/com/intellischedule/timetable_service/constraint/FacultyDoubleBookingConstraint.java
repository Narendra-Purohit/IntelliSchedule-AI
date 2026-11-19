package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.Timetable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FacultyDoubleBookingConstraint implements TimetableConstraint {

    @Override
    public String getConstraintName() {
        return "FacultyDoubleBooking";
    }

    @Override
    public boolean isHardConstraint() {
        return true;
    }

    @Override
    public List<String> validate(List<Timetable> schedule) {
        List<String> violations = new ArrayList<>();
        Map<String, Timetable> seen = new HashMap<>();

        for (Timetable t : schedule) {
            if (t.getFaculty() == null || t.getTimeSlot() == null) continue;
            String key = t.getDayOfWeek() + ":" + t.getTimeSlot().getId() + ":" + t.getFaculty().getId();
            if (seen.containsKey(key)) {
                violations.add(String.format("Faculty '%s' double-booked on %s at slot %s",
                        t.getFaculty().getName(), t.getDayOfWeek(), t.getTimeSlot().getStartTime()));
            } else {
                seen.put(key, t);
            }
        }
        return violations;
    }
}
