package com.intellischedule.timetable_service.service;

import com.intellischedule.timetable_service.constraint.*;
import com.intellischedule.timetable_service.dto.ConstraintEvaluationResult;
import com.intellischedule.timetable_service.entity.Timetable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleQualityScorer {

    private final List<TimetableConstraint> constraints;

    public ScheduleQualityScorer(List<TimetableConstraint> constraints) {
        this.constraints = constraints;
    }

    public ConstraintEvaluationResult evaluate(List<Timetable> schedule) {
        List<String> hardViolations = new ArrayList<>();
        List<String> softWarnings = new ArrayList<>();

        for (TimetableConstraint c : constraints) {
            List<String> result = c.validate(schedule);
            if (c.isHardConstraint()) {
                hardViolations.addAll(result);
            } else {
                softWarnings.addAll(result);
            }
        }

        boolean isFeasible = hardViolations.isEmpty();
        double fitness = isFeasible ? Math.max(0.0, 100.0 - (softWarnings.size() * 5.0)) : 0.0;

        return new ConstraintEvaluationResult(
                hardViolations.size(),
                softWarnings.size(),
                isFeasible,
                fitness,
                hardViolations,
                softWarnings
        );
    }
}
