package com.intellischedule.timetable_service.controller;

import com.intellischedule.timetable_service.dto.ConstraintEvaluationResult;
import com.intellischedule.timetable_service.entity.Timetable;
import com.intellischedule.timetable_service.repository.TimetableRepository;
import com.intellischedule.timetable_service.service.ScheduleQualityScorer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/timetables")
public class TimetableConflictController {

    private final TimetableRepository timetableRepository;
    private final ScheduleQualityScorer scorer;

    public TimetableConflictController(TimetableRepository timetableRepository, ScheduleQualityScorer scorer) {
        this.timetableRepository = timetableRepository;
        this.scorer = scorer;
    }

    @GetMapping("/conflicts")
    public ResponseEntity<ConstraintEvaluationResult> auditCurrentSchedule() {
        List<Timetable> schedule = timetableRepository.findAll();
        return ResponseEntity.ok(scorer.evaluate(schedule));
    }
}
