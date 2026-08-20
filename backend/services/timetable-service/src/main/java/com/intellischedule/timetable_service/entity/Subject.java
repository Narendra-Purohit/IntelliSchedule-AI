package com.intellischedule.timetable_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects")
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

    @Column(name = "periods_per_week", nullable = false)
    private Integer periodsPerWeek;

    public Subject() {
    }

    public Subject(
            String code,
            String name,
            String type,
            Integer credits,
            Integer periodsPerWeek
    ) {
        this.code = code;
        this.name = name;
        this.type = type;
        this.credits = credits;
        this.periodsPerWeek = periodsPerWeek;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getPeriodsPerWeek() {
        return periodsPerWeek;
    }

    public void setPeriodsPerWeek(Integer periodsPerWeek) {
        this.periodsPerWeek = periodsPerWeek;
    }
}