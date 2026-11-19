package com.intellischedule.timetable_service.fixture;

import com.intellischedule.timetable_service.entity.*;

public final class TimetableTestFixture {

    public static Faculty createFaculty(Long id, String name) {
        Faculty f = new Faculty();
        f.setId(id);
        f.setName(name);
        f.setEmail(name.toLowerCase() + "@college.edu");
        f.setMaxHoursPerWeek(16);
        return f;
    }

    public static Room createRoom(Long id, String roomNum, int cap) {
        Room r = new Room();
        r.setId(id);
        r.setRoomNumber(roomNum);
        r.setCapacity(cap);
        return r;
    }

    public static TimeSlot createSlot(Long id, String day, String start, String end) {
        TimeSlot s = new TimeSlot();
        s.setId(id);
        s.setDayOfWeek(day);
        s.setStartTime(start);
        s.setEndTime(end);
        return s;
    }
}
