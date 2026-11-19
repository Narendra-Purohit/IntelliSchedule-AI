package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.*;
import com.intellischedule.timetable_service.fixture.TimetableTestFixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FacultyDoubleBookingConstraintTest {

    private final FacultyDoubleBookingConstraint constraint = new FacultyDoubleBookingConstraint();

    @Test
    void shouldDetectFacultyDoubleBooking() {
        Faculty fac = TimetableTestFixture.createFaculty(1L, "Dr. Smith");
        TimeSlot slot = TimetableTestFixture.createSlot(10L, "Monday", "09:00", "10:00");

        Timetable t1 = new Timetable();
        t1.setFaculty(fac);
        t1.setTimeSlot(slot);
        t1.setDayOfWeek("Monday");

        Timetable t2 = new Timetable();
        t2.setFaculty(fac);
        t2.setTimeSlot(slot);
        t2.setDayOfWeek("Monday");

        List<String> violations = constraint.validate(List.of(t1, t2));
        assertEquals(1, violations.size());
        assertTrue(violations.get(0).contains("double-booked"));
    }
}
