package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.*;
import com.intellischedule.timetable_service.fixture.TimetableTestFixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomCollisionConstraintTest {

    private final RoomCollisionConstraint constraint = new RoomCollisionConstraint();

    @Test
    void shouldDetectRoomCollision() {
        Room room = TimetableTestFixture.createRoom(1L, "LH-101", 60);
        TimeSlot slot = TimetableTestFixture.createSlot(10L, "Tuesday", "10:00", "11:00");

        Timetable t1 = new Timetable();
        t1.setRoom(room);
        t1.setTimeSlot(slot);
        t1.setDayOfWeek("Tuesday");

        Timetable t2 = new Timetable();
        t2.setRoom(room);
        t2.setTimeSlot(slot);
        t2.setDayOfWeek("Tuesday");

        List<String> violations = constraint.validate(List.of(t1, t2));
        assertEquals(1, violations.size());
        assertTrue(violations.get(0).contains("occupied simultaneously"));
    }
}
