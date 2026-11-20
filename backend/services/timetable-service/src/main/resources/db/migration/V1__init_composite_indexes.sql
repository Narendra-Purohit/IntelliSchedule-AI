-- Composite indexes for sub-millisecond collision detection and fast lookups
-- Schema: intelligent_timetable

CREATE INDEX idx_tt_slot_room ON timetables (day_of_week, time_slot_id, room_id);
CREATE INDEX idx_tt_slot_faculty ON timetables (day_of_week, time_slot_id, faculty_id);
CREATE INDEX idx_tt_section_day ON timetables (section_id, day_of_week);
CREATE INDEX idx_sub_code ON subjects (code);
CREATE INDEX idx_fac_name ON faculty (name);
