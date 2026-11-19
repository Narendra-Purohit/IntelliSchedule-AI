package com.intellischedule.report_service.dto;

import java.io.Serializable;
import java.util.List;

public record ConflictAuditDTO(
    int totalSlotsEvaluated,
    int conflictCount,
    boolean isConflictFree,
    List<String> conflictDetails
) implements Serializable {}
