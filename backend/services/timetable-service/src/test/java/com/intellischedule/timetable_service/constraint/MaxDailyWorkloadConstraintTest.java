package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.*;
import com.intellischedule.timetable_service.fixture.TimetableTestFixture;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaxDailyWorkloadConstraintTest {

    private final MaxDailyWorkloadConstraint constraint = new MaxDailyWorkloadConstraint();

    @Test
    void shouldWarnWhenFacultyHasMoreThanFourClassesInOneDay() {
        Faculty fac = TimetableTestFixture.createFaculty(5L, "Dr. Mehta");
        List<Timetable> schedule = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Timetable t = new Timetable();
            t.setFaculty(fac);
            t.setDayOfWeek("Wednesday");
            schedule.add(t);
        }

        List<String> warnings = constraint.validate(schedule);
        assertEquals(1, warnings.size());
        assertTrue(warnings.get(0).contains("exceeds recommended"));
    }
}
