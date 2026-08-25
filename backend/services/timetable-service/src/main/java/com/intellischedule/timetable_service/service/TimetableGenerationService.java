package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.entity.FacultyAllocation;
import com.intellischedule.timetable_service.entity.Room;
import com.intellischedule.timetable_service.entity.SubjectAllocation;
import com.intellischedule.timetable_service.entity.TimeSlot;
import com.intellischedule.timetable_service.entity.Timetable;
import com.intellischedule.timetable_service.repository.FacultyAllocationRepository;
import com.intellischedule.timetable_service.repository.RoomRepository;
import com.intellischedule.timetable_service.repository.SubjectAllocationRepository;
import com.intellischedule.timetable_service.repository.TimeSlotRepository;
import com.intellischedule.timetable_service.repository.TimetableRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimetableGenerationService {

    private final SubjectAllocationRepository subjectAllocationRepository;
    private final FacultyAllocationRepository facultyAllocationRepository;
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TimetableRepository timetableRepository;

    public TimetableGenerationService(
            SubjectAllocationRepository subjectAllocationRepository,
            FacultyAllocationRepository facultyAllocationRepository,
            RoomRepository roomRepository,
            TimeSlotRepository timeSlotRepository,
            TimetableRepository timetableRepository
    ) {
        this.subjectAllocationRepository = subjectAllocationRepository;
        this.facultyAllocationRepository = facultyAllocationRepository;
        this.roomRepository = roomRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.timetableRepository = timetableRepository;
    }

    public List<Timetable> generateTimetable() {

        List<SubjectAllocation> subjectAllocations =
                subjectAllocationRepository.findAll();

        List<FacultyAllocation> facultyAllocations =
                facultyAllocationRepository.findAll();

        List<Room> rooms =
                roomRepository.findAll();

        List<TimeSlot> timeSlots =
                timeSlotRepository.findAll();

        List<Timetable> generatedTimetables =
                new ArrayList<>();

        for (SubjectAllocation subjectAllocation : subjectAllocations) {

            FacultyAllocation facultyAllocation =
                    findFacultyAllocation(
                            subjectAllocation,
                            facultyAllocations
                    );

            // Faculty allocation not available
            if (facultyAllocation == null) {
                continue;
            }

            int requiredHours =
                    getRequiredHours(subjectAllocation);

            int generatedHours = 0;

            for (TimeSlot timeSlot : timeSlots) {

                if (generatedHours >= requiredHours) {
                    break;
                }

                Long facultyId =
                        facultyAllocation.getFaculty().getId();

                Long sectionId =
                        subjectAllocation.getSection().getId();

                // -------------------------------------------------
                // Faculty Clash
                // -------------------------------------------------
                if (timetableRepository
                        .existsByFacultyAllocationFacultyIdAndTimeSlotId(
                                facultyId,
                                timeSlot.getId()
                        )) {
                    continue;
                }

                // -------------------------------------------------
                // Section Clash
                // -------------------------------------------------
                if (timetableRepository
                        .existsBySubjectAllocationSectionIdAndTimeSlotId(
                                sectionId,
                                timeSlot.getId()
                        )) {
                    continue;
                }

                // -------------------------------------------------
                // Faculty Availability
                // -------------------------------------------------
                if (!isFacultyAvailable(
                        facultyAllocation,
                        timeSlot
                )) {
                    continue;
                }

                // -------------------------------------------------
                // Find Suitable Room
                // -------------------------------------------------
                Room room =
                        findAvailableRoom(
                                subjectAllocation,
                                timeSlot,
                                rooms
                        );

                if (room == null) {
                    continue;
                }

                // -------------------------------------------------
                // Create Timetable
                // -------------------------------------------------
                Timetable timetable = new Timetable();

                timetable.setSubjectAllocation(
                        subjectAllocation
                );

                timetable.setFacultyAllocation(
                        facultyAllocation
                );

                timetable.setRoom(room);

                timetable.setTimeSlot(timeSlot);

                Timetable saved =
                        timetableRepository.save(timetable);

                generatedTimetables.add(saved);

                generatedHours++;
            }
        }

        return generatedTimetables;
    }

    // =========================================================
    // Calculate Required Hours
    // =========================================================

    private int getRequiredHours(
            SubjectAllocation subjectAllocation
    ) {

        if (subjectAllocation == null
                || subjectAllocation.getSubject() == null) {
            return 1;
        }

        Integer lectureHours =
                subjectAllocation
                        .getSubject()
                        .getLectureHoursPerWeek();

        Integer practicalHours =
                subjectAllocation
                        .getSubject()
                        .getPracticalHoursPerWeek();

        Integer tutorialHours =
                subjectAllocation
                        .getSubject()
                        .getTutorialHoursPerWeek();

        int totalHours = 0;

        if (lectureHours != null && lectureHours > 0) {
            totalHours += lectureHours;
        }

        if (practicalHours != null && practicalHours > 0) {
            totalHours += practicalHours;
        }

        if (tutorialHours != null && tutorialHours > 0) {
            totalHours += tutorialHours;
        }

        return totalHours > 0 ? totalHours : 1;
    }

    // =========================================================
    // Find Faculty Allocation
    // =========================================================

    private FacultyAllocation findFacultyAllocation(
            SubjectAllocation subjectAllocation,
            List<FacultyAllocation> facultyAllocations
    ) {

        if (subjectAllocation == null
                || subjectAllocation.getId() == null
                || facultyAllocations == null) {
            return null;
        }

        for (FacultyAllocation allocation :
                facultyAllocations) {

            if (allocation == null
                    || allocation.getSubjectAllocation() == null
                    || allocation.getSubjectAllocation().getId() == null) {
                continue;
            }

            if (subjectAllocation.getId().equals(
                    allocation.getSubjectAllocation().getId()
            )) {
                return allocation;
            }
        }

        return null;
    }

    // =========================================================
    // Find Available Room
    // =========================================================

    private Room findAvailableRoom(
            SubjectAllocation subjectAllocation,
            TimeSlot timeSlot,
            List<Room> rooms
    ) {

        if (subjectAllocation == null
                || subjectAllocation.getSection() == null
                || subjectAllocation.getSubject() == null
                || timeSlot == null
                || rooms == null) {
            return null;
        }

        Integer studentCount =
                subjectAllocation
                        .getSection()
                        .getStudentCount();

        String subjectType =
                subjectAllocation
                        .getSubject()
                        .getType();

        String requiredRoomType =
                getRequiredRoomType(subjectType);

        for (Room room : rooms) {

            if (room == null) {
                continue;
            }

            // -------------------------------------------------
            // Capacity Check
            // -------------------------------------------------
            if (studentCount != null
                    && room.getCapacity() < studentCount) {
                continue;
            }

            // -------------------------------------------------
            // Room Type Check
            // -------------------------------------------------
            if (requiredRoomType != null
                    && !requiredRoomType.equalsIgnoreCase(
                            room.getRoomType())) {
                continue;
            }

            // -------------------------------------------------
            // Room Clash Check
            // -------------------------------------------------
            if (timetableRepository
                    .existsByRoomIdAndTimeSlotId(
                            room.getId(),
                            timeSlot.getId()
                    )) {
                continue;
            }

            return room;
        }

        return null;
    }

    // =========================================================
    // Subject Type -> Room Type
    // =========================================================

    private String getRequiredRoomType(
            String subjectType
    ) {

        if (subjectType == null
                || subjectType.isBlank()) {
            return "Classroom";
        }

        return switch (subjectType.trim().toLowerCase()) {

            case "practical",
                 "lab",
                 "laboratory" ->
                    "Laboratory";

            case "theory",
                 "tutorial" ->
                    "Classroom";

            default ->
                    "Classroom";
        };
    }

    // =========================================================
    // Faculty Availability
    // =========================================================

    private boolean isFacultyAvailable(
            FacultyAllocation facultyAllocation,
            TimeSlot timeSlot
    ) {

        if (facultyAllocation == null
                || facultyAllocation.getFaculty() == null
                || timeSlot == null) {
            return false;
        }

        String availableDays =
                facultyAllocation
                        .getFaculty()
                        .getAvailableDays();

        if (availableDays != null
                && !availableDays.isBlank()) {

            String targetDay =
                    normalizeDay(
                            timeSlot.getDayOfWeek()
                    );

            boolean dayAvailable = false;

            String[] days =
                    availableDays.split(",");

            for (String day : days) {

                if (normalizeDay(day)
                        .equals(targetDay)) {

                    dayAvailable = true;
                    break;
                }
            }

            if (!dayAvailable) {
                return false;
            }
        }

        String unavailableSlots =
                facultyAllocation
                        .getFaculty()
                        .getUnavailableSlots();

        return !isUnavailableSlot(
                unavailableSlots,
                timeSlot
        );
    }

    // =========================================================
    // Unavailable Faculty Slot
    // =========================================================

    private boolean isUnavailableSlot(
            String unavailableSlots,
            TimeSlot timeSlot
    ) {

        if (unavailableSlots == null
                || unavailableSlots.isBlank()
                || timeSlot == null) {
            return false;
        }

        String[] slots =
                unavailableSlots.split(",");

        String targetDay =
                normalizeDay(
                        timeSlot.getDayOfWeek()
                );

        String targetStart =
                normalizeTime(
                        timeSlot.getStartTime()
                );

        String targetEnd =
                normalizeTime(
                        timeSlot.getEndTime()
                );

        for (String slot : slots) {

            slot = slot.trim();

            if (slot.isBlank()) {
                continue;
            }

            String[] parts =
                    slot.split("\\s+");

            if (parts.length != 2) {
                continue;
            }

            String unavailableDay =
                    normalizeDay(parts[0]);

            if (!unavailableDay.equals(targetDay)) {
                continue;
            }

            String[] times =
                    parts[1].split("-");

            if (times.length != 2) {
                continue;
            }

            String unavailableStart =
                    normalizeTime(times[0]);

            String unavailableEnd =
                    normalizeTime(times[1]);

            if (timesOverlap(
                    targetStart,
                    targetEnd,
                    unavailableStart,
                    unavailableEnd
            )) {
                return true;
            }
        }

        return false;
    }

    // =========================================================
    // Normalize Time
    // =========================================================

    private String normalizeTime(String time) {

        if (time == null) {
            return "";
        }

        String value = time.trim();

        if (value.matches("\\d{4}")) {

            return value.substring(0, 2)
                    + ":"
                    + value.substring(2);
        }

        return value;
    }

    // =========================================================
    // Time Overlap
    // =========================================================

    private boolean timesOverlap(
            String start1,
            String end1,
            String start2,
            String end2
    ) {

        if (start1.isBlank()
                || end1.isBlank()
                || start2.isBlank()
                || end2.isBlank()) {
            return false;
        }

        int s1 = timeToMinutes(start1);
        int e1 = timeToMinutes(end1);

        int s2 = timeToMinutes(start2);
        int e2 = timeToMinutes(end2);

        return s1 < e2 && s2 < e1;
    }

    // =========================================================
    // Time -> Minutes
    // =========================================================

    private int timeToMinutes(String time) {

        String[] parts =
                time.split(":");

        if (parts.length != 2) {
            return 0;
        }

        int hours =
                Integer.parseInt(parts[0]);

        int minutes =
                Integer.parseInt(parts[1]);

        return hours * 60 + minutes;
    }

    // =========================================================
    // Normalize Day
    // =========================================================

    private String normalizeDay(String day) {

        if (day == null) {
            return "";
        }

        return switch (
                day.trim().toLowerCase()
        ) {

            case "mon",
                 "monday" ->
                    "monday";

            case "tue",
                 "tues",
                 "tuesday" ->
                    "tuesday";

            case "wed",
                 "wednesday" ->
                    "wednesday";

            case "thu",
                 "thur",
                 "thurs",
                 "thursday" ->
                    "thursday";

            case "fri",
                 "friday" ->
                    "friday";

            case "sat",
                 "saturday" ->
                    "saturday";

            case "sun",
                 "sunday" ->
                    "sunday";

            default ->
                    day.trim().toLowerCase();
        };
    }
}