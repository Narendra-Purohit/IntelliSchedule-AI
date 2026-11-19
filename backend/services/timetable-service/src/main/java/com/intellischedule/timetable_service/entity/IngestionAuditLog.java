package com.intellischedule.timetable_service.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ingestion_audit_logs")
public class IngestionAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filename;

    private String program;
    private String semester;
    private int subjectsCount;
    private int facultyCount;

    @Column(nullable = false)
    private LocalDateTime ingestedAt = LocalDateTime.now();

    public IngestionAuditLog() {}

    public IngestionAuditLog(String filename, String program, String semester, int subjectsCount, int facultyCount) {
        this.filename = filename;
        this.program = program;
        this.semester = semester;
        this.subjectsCount = subjectsCount;
        this.facultyCount = facultyCount;
    }

    public Long getId() { return id; }
    public String getFilename() { return filename; }
    public String getProgram() { return program; }
    public String getSemester() { return semester; }
    public int getSubjectsCount() { return subjectsCount; }
    public int getFacultyCount() { return facultyCount; }
    public LocalDateTime getIngestedAt() { return ingestedAt; }
}
