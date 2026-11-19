package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.constraint.*;
import com.intellischedule.timetable_service.dto.ConstraintEvaluationResult;
import com.intellischedule.timetable_service.entity.*;
import com.intellischedule.timetable_service.fixture.TimetableTestFixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScheduleQualityScorerTest {

    @Test
    void shouldGive100FitnessToCleanSchedule() {
        ScheduleQualityScorer scorer = new ScheduleQualityScorer(List.of(
                new FacultyDoubleBookingConstraint(),
                new RoomCollisionConstraint()
        ));

        Faculty f1 = TimetableTestFixture.createFaculty(1L, "Dr. Rao");
        Faculty f2 = TimetableTestFixture.createFaculty(2L, "Prof. Patel");
        TimeSlot slot = TimetableTestFixture.createSlot(1L, "Monday", "09:00", "10:00");
        Room r1 = TimetableTestFixture.createRoom(1L, "LH-1", 60);
        Room r2 = TimetableTestFixture.createRoom(2L, "LH-2", 60);

        Timetable t1 = new Timetable(); t1.setFaculty(f1); t1.setTimeSlot(slot); t1.setRoom(r1); t1.setDayOfWeek("Monday");
        Timetable t2 = new Timetable(); t2.setFaculty(f2); t2.setTimeSlot(slot); t2.setRoom(r2); t2.setDayOfWeek("Monday");

        ConstraintEvaluationResult res = scorer.evaluate(List.of(t1, t2));
        assertTrue(res.isFeasible());
        assertEquals(100.0, res.fitnessScore());
    }
}
