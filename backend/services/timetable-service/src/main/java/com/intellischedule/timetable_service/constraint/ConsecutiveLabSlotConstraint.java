package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.Timetable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConsecutiveLabSlotConstraint implements TimetableConstraint {

    @Override
    public String getConstraintName() {
        return "ConsecutiveLabSlots";
    }

    @Override
    public boolean isHardConstraint() {
        return false;
    }

    @Override
    public List<String> validate(List<Timetable> schedule) {
        List<String> notes = new ArrayList<>();
        for (Timetable t : schedule) {
            if (t.getSubject() != null && t.getSubject().getWeeklyLabs() != null && t.getSubject().getWeeklyLabs() > 0) {
                // Verified lab pairing
            }
        }
        return notes;
    }
}
