package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.Timetable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoomCapacityConstraint implements TimetableConstraint {

    @Override
    public String getConstraintName() {
        return "RoomCapacity";
    }

    @Override
    public boolean isHardConstraint() {
        return false;
    }

    @Override
    public List<String> validate(List<Timetable> schedule) {
        List<String> warnings = new ArrayList<>();
        for (Timetable t : schedule) {
            if (t.getRoom() != null && t.getSection() != null) {
                if (t.getRoom().getCapacity() != null && t.getRoom().getCapacity() < 40) {
                    warnings.add(String.format("Room '%s' capacity (%d) may be tight for section '%s'",
                            t.getRoom().getRoomNumber(), t.getRoom().getCapacity(), t.getSection().getName()));
                }
            }
        }
        return warnings;
    }
}
