package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.Timetable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RoomCollisionConstraint implements TimetableConstraint {

    @Override
    public String getConstraintName() {
        return "RoomCollision";
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
            if (t.getRoom() == null || t.getTimeSlot() == null) continue;
            String key = t.getDayOfWeek() + ":" + t.getTimeSlot().getId() + ":" + t.getRoom().getId();
            if (seen.containsKey(key)) {
                violations.add(String.format("Room '%s' occupied simultaneously on %s at slot %s",
                        t.getRoom().getRoomNumber(), t.getDayOfWeek(), t.getTimeSlot().getStartTime()));
            } else {
                seen.put(key, t);
            }
        }
        return violations;
    }
}
