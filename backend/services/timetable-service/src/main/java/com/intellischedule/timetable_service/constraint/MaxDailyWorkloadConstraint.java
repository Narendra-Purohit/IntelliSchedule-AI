package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.Timetable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MaxDailyWorkloadConstraint implements TimetableConstraint {

    private static final int MAX_DAILY_HOURS = 4;

    @Override
    public String getConstraintName() {
        return "MaxDailyWorkload";
    }

    @Override
    public boolean isHardConstraint() {
        return false;
    }

    @Override
    public List<String> validate(List<Timetable> schedule) {
        List<String> warnings = new ArrayList<>();
        Map<String, Integer> counts = new HashMap<>();

        for (Timetable t : schedule) {
            if (t.getFaculty() == null) continue;
            String key = t.getDayOfWeek() + ":" + t.getFaculty().getId();
            counts.put(key, counts.getOrDefault(key, 0) + 1);
        }

        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            if (e.getValue() > MAX_DAILY_HOURS) {
                warnings.add(String.format("Faculty allocation %s exceeds recommended %d classes/day (count: %d)",
                        e.getKey(), MAX_DAILY_HOURS, e.getValue()));
            }
        }
        return warnings;
    }
}
