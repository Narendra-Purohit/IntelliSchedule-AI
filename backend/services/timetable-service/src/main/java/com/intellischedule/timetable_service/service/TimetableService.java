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

import java.util.List;

@Service
public class TimetableService {

    private final TimetableRepository timetableRepository;
    private final SubjectAllocationRepository subjectAllocationRepository;
    private final FacultyAllocationRepository facultyAllocationRepository;
    private final RoomRepository roomRepository;
    private final TimeSlotRepository timeSlotRepository;

    public TimetableService(
            TimetableRepository timetableRepository,
            SubjectAllocationRepository subjectAllocationRepository,
            FacultyAllocationRepository facultyAllocationRepository,
            RoomRepository roomRepository,
            TimeSlotRepository timeSlotRepository
    ) {
        this.timetableRepository = timetableRepository;
        this.subjectAllocationRepository = subjectAllocationRepository;
        this.facultyAllocationRepository = facultyAllocationRepository;
        this.roomRepository = roomRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    // CREATE
    public Timetable createTimetable(
            Long subjectAllocationId,
            Long facultyAllocationId,
            Long roomId,
            Long timeSlotId
    ) {

        SubjectAllocation subjectAllocation =
                subjectAllocationRepository.findById(subjectAllocationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Subject allocation not found: "
                                                + subjectAllocationId
                                )
                        );

        FacultyAllocation facultyAllocation =
                facultyAllocationRepository.findById(facultyAllocationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Faculty allocation not found: "
                                                + facultyAllocationId
                                )
                        );

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Room not found: " + roomId
                        )
                );

        TimeSlot timeSlot = timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Time slot not found: " + timeSlotId
                        )
                );

        // Make sure faculty allocation belongs to the same subject allocation
        if (!facultyAllocation.getSubjectAllocation()
                .getId()
                .equals(subjectAllocationId)) {

            throw new IllegalArgumentException(
                    "Faculty allocation does not belong to the subject allocation"
            );
        }

        Long facultyId =
                facultyAllocation.getFaculty().getId();

        Long sectionId =
                subjectAllocation.getSection().getId();

        // Faculty clash
        if (timetableRepository
                .existsByFacultyAllocationFacultyIdAndTimeSlotId(
                        facultyId,
                        timeSlotId
                )) {

            throw new IllegalArgumentException(
                    "Faculty already has a timetable entry in this time slot"
            );
        }

        // Room clash
        if (timetableRepository
                .existsByRoomIdAndTimeSlotId(
                        roomId,
                        timeSlotId
                )) {

            throw new IllegalArgumentException(
                    "Room already has a timetable entry in this time slot"
            );
        }

        // Section clash
        if (timetableRepository
                .existsBySubjectAllocationSectionIdAndTimeSlotId(
                        sectionId,
                        timeSlotId
                )) {

            throw new IllegalArgumentException(
                    "Section already has a timetable entry in this time slot"
            );
        }

        // Room capacity check
        Integer studentCount =
                subjectAllocation
                        .getSection()
                        .getStudentCount();

        if (studentCount != null
                && room.getCapacity() < studentCount) {

            throw new IllegalArgumentException(
                    "Room capacity is not enough for the section"
            );
        }

        // Faculty availability check
        String availableDays =
                facultyAllocation
                        .getFaculty()
                        .getAvailableDays();

        if (availableDays != null
                && !availableDays.isBlank()
                && !isDayAvailable(
                        availableDays,
                        timeSlot.getDayOfWeek()
                )) {

            throw new IllegalArgumentException(
                    "Faculty is not available on "
                            + timeSlot.getDayOfWeek()
            );
        }

        // Faculty unavailable slot check
        String unavailableSlots =
                facultyAllocation
                        .getFaculty()
                        .getUnavailableSlots();

        if (unavailableSlots != null
                && !unavailableSlots.isBlank()
                && isUnavailableSlot(
                        unavailableSlots,
                        timeSlot
                )) {

            throw new IllegalArgumentException(
                    "Faculty is unavailable during this time slot"
            );
        }

        Timetable timetable = new Timetable();

        timetable.setSubjectAllocation(subjectAllocation);
        timetable.setFacultyAllocation(facultyAllocation);
        timetable.setRoom(room);
        timetable.setTimeSlot(timeSlot);

        return timetableRepository.save(timetable);
    }

    // GET ALL
    public List<Timetable> getAllTimetables() {
        return timetableRepository.findAll();
    }

    // GET BY ID
    public Timetable getTimetableById(Long id) {

        return timetableRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Timetable not found: " + id
                        )
                );
    }

    // GET BY SECTION
    public List<Timetable> getTimetablesBySection(
            Long sectionId
    ) {

        return timetableRepository
                .findBySubjectAllocationSectionId(sectionId);
    }

    // GET BY FACULTY
    public List<Timetable> getTimetablesByFaculty(
            Long facultyId
    ) {

        return timetableRepository
                .findByFacultyAllocationFacultyId(facultyId);
    }

    // GET BY ROOM
    public List<Timetable> getTimetablesByRoom(
            Long roomId
    ) {

        return timetableRepository
                .findByRoomId(roomId);
    }

    // GET BY TIME SLOT
    public List<Timetable> getTimetablesByTimeSlot(
            Long timeSlotId
    ) {

        return timetableRepository
                .findByTimeSlotId(timeSlotId);
    }

    // DELETE
    public void deleteTimetable(Long id) {

        Timetable timetable = getTimetableById(id);

        timetableRepository.delete(timetable);
    }

    private boolean isDayAvailable(
        String availableDays,
        String dayOfWeek
) {

    if (availableDays == null || dayOfWeek == null) {
        return false;
    }

    String targetDay = normalizeDay(dayOfWeek);

    String[] days = availableDays.split(",");

    for (String day : days) {

        if (normalizeDay(day).equals(targetDay)) {
            return true;
        }
    }

    return false;
}

private String normalizeDay(String day) {

    if (day == null) {
        return "";
    }

    String value = day.trim().toLowerCase();

    return switch (value) {
        case "mon", "monday" -> "monday";
        case "tue", "tues", "tuesday" -> "tuesday";
        case "wed", "wednesday" -> "wednesday";
        case "thu", "thur", "thurs", "thursday" -> "thursday";
        case "fri", "friday" -> "friday";
        case "sat", "saturday" -> "saturday";
        case "sun", "sunday" -> "sunday";
        default -> value;
    };
}

    private boolean isUnavailableSlot(
            String unavailableSlots,
            TimeSlot timeSlot
    ) {

        if (timeSlot.getDayOfWeek() == null) {
            return false;
        }

        String targetDay =
                timeSlot.getDayOfWeek()
                        .trim()
                        .toLowerCase();

        String targetStart =
                timeSlot.getStartTime();

        String targetEnd =
                timeSlot.getEndTime();

        String[] entries =
                unavailableSlots.split(",");

        for (String entry : entries) {

            String value = entry.trim();

            String lowerValue =
                    value.toLowerCase();

            if (!lowerValue.startsWith(targetDay)) {
                continue;
            }

            if (targetStart != null
                    && lowerValue.contains(
                            targetStart.toLowerCase()
                    )) {

                return true;
            }

            if (targetEnd != null
                    && lowerValue.contains(
                            targetEnd.toLowerCase()
                    )) {

                return true;
            }
        }

        return false;
    }
}