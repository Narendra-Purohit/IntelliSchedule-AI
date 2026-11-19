package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.Timetable;
import java.util.List;

public interface TimetableConstraint {
    String getConstraintName();
    boolean isHardConstraint();
    List<String> validate(List<Timetable> schedule);
}
