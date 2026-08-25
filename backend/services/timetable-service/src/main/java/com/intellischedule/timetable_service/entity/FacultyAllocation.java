package com.intellischedule.timetable_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "faculty_allocations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_faculty_subject_allocation",
                        columnNames = {"faculty_id", "subject_allocation_id"}
                )
        }
)
@Getter
@Setter
public class FacultyAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_allocation_id", nullable = false)
    private SubjectAllocation subjectAllocation;

    public FacultyAllocation() {
    }
}