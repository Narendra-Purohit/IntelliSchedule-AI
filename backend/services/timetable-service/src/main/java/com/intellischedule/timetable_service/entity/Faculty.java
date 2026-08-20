package com.intellischedule.timetable_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "faculty")
@Getter
@Setter
public class Faculty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "faculty_id", nullable = false, unique = true)
    private String facultyId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String department;

    @Column(name = "available_days", nullable = false)
    private String availableDays;

    @Column(name = "unavailable_slots")
    private String unavailableSlots;
}