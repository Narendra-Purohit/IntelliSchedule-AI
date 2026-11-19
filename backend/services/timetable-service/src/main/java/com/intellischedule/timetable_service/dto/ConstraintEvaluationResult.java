package com.intellischedule.timetable_service.dto;

import java.util.List;

public record ConstraintEvaluationResult(
    int totalHardViolations,
    int totalSoftViolations,
    boolean isFeasible,
    double fitnessScore,
    List<String> hardViolations,
    List<String> softWarnings
) {}
