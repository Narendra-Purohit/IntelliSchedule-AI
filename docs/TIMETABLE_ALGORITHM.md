# Timetable Scheduling Heuristics & Constraints

## Overview
The scheduling engine in `timetable-service` operates as a **heuristic constraint satisfaction problem (CSP)** solver.

### 1. Hard Constraints (Must NEVER be violated)
- **Faculty Exclusivity**: A faculty member cannot be scheduled for two concurrent classes.
- **Room Exclusivity**: A room or laboratory cannot host two sections at the identical timeslot.
- **Section Exclusivity**: A student cohort/section cannot have overlapping lectures.

### 2. Soft Constraints (Optimization Objectives)
- **Room Capacity**: Maximize section size to classroom seating ratio.
- **Faculty Workload Balance**: Spread teaching allocations evenly; limit daily hours to $\le 4$.
- **Lab Slot Continuity**: Ensure practical laboratory sessions are assigned in consecutive 2-hour slots.

### 3. Fitness Scoring
$$\text{Fitness} = \begin{cases} 0 & \text{if hard violations} > 0 \\ \max(0, 100 - (\text{soft warnings} \times 5)) & \text{otherwise} \end{cases}$$
