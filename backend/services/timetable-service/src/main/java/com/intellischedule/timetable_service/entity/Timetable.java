package com.intellischedule.timetable_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "timetables",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_timetable_subject_time",
                        columnNames = {
                                "subject_allocation_id",
                                "time_slot_id"
                        }
                )
        }
)
@Getter
@Setter
public class Timetable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_allocation_id", nullable = false)
    private SubjectAllocation subjectAllocation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_allocation_id", nullable = false)
    private FacultyAllocation facultyAllocation;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "time_slot_id", nullable = false)
    private TimeSlot timeSlot;

    public Timetable() {
    }
}