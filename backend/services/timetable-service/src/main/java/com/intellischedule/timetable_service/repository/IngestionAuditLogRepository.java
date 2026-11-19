package com.intellischedule.timetable_service.repository;

import com.intellischedule.timetable_service.entity.IngestionAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngestionAuditLogRepository extends JpaRepository<IngestionAuditLog, Long> {
    List<IngestionAuditLog> findByProgram(String program);
}
