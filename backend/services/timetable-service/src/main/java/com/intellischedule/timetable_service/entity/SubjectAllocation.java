package com.intellischedule.timetable_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "subject_allocations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_section_subject",
                        columnNames = {"section_id", "subject_id"}
                )
        }
)
@Getter
@Setter
public class SubjectAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public SubjectAllocation() {
    }
}