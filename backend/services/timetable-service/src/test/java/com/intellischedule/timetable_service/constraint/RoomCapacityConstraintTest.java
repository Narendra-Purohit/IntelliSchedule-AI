package com.intellischedule.timetable_service.constraint;

import com.intellischedule.timetable_service.entity.*;
import com.intellischedule.timetable_service.fixture.TimetableTestFixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomCapacityConstraintTest {

    private final RoomCapacityConstraint constraint = new RoomCapacityConstraint();

    @Test
    void shouldWarnOnSmallRoomCapacity() {
        Room room = TimetableTestFixture.createRoom(1L, "Lab-3", 25);
        Section sec = new Section();
        sec.setName("Batch-CS-A");

        Timetable t = new Timetable();
        t.setRoom(room);
        t.setSection(sec);

        List<String> warnings = constraint.validate(List.of(t));
        assertEquals(1, warnings.size());
        assertTrue(warnings.get(0).contains("capacity"));
    }
}
