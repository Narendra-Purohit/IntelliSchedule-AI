package com.intellischedule.timetable_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "subjects")
@Getter
@Setter
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer credits;

    @Column(name = "lecture_hours_per_week", nullable = false)
    private Integer lectureHoursPerWeek;

    @Column(name = "tutorial_hours_per_week")
    private Integer tutorialHoursPerWeek = 0;

    @Column(name = "practical_hours_per_week")
    private Integer practicalHoursPerWeek = 0;

    public Subject() {
    }
}