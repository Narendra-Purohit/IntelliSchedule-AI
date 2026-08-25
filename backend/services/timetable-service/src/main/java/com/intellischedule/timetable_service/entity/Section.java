package com.intellischedule.timetable_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "sections",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_semester_section",
                        columnNames = {"semester_id", "section_name"}
                )
        }
)
@Getter
@Setter
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    @Column(nullable = false)
    private Integer studentCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    public Section() {
    }
}