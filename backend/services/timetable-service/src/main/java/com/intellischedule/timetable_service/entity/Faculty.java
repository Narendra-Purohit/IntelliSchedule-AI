package com.intellischedule.timetable_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "faculty")
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

    public Faculty() {
    }

    public Faculty(
            String facultyId,
            String name,
            String department,
            String availableDays,
            String unavailableSlots
    ) {
        this.facultyId = facultyId;
        this.name = name;
        this.department = department;
        this.availableDays = availableDays;
        this.unavailableSlots = unavailableSlots;
    }

    public Long getId() {
        return id;
    }

    public String getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(String facultyId) {
        this.facultyId = facultyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    public String getUnavailableSlots() {
        return unavailableSlots;
    }

    public void setUnavailableSlots(String unavailableSlots) {
        this.unavailableSlots = unavailableSlots;
    }
}