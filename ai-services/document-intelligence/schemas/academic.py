from typing import List, Optional

from pydantic import BaseModel, Field


class Institution(BaseModel):
    name: str
    department: Optional[str] = None


class AcademicInfo(BaseModel):
    program: Optional[str] = None
    semester: Optional[str] = None
    academic_year: Optional[str] = None


class Section(BaseModel):
    course: str
    semester: str
    section: str
    students: int
    class_coordinator: Optional[str] = None


class Subject(BaseModel):
    code: str
    name: str
    type: str
    credits: int
    periods_per_week: int
    faculty: Optional[str] = None
    room_type: Optional[str] = None


class Faculty(BaseModel):
    faculty_id: str
    name: str
    department: str
    available_days: List[str] = Field(default_factory=list)
    unavailable_slots: List[str] = Field(default_factory=list)


class Room(BaseModel):
    room_id: str
    name: str
    type: str
    capacity: int
    available_days: List[str] = Field(default_factory=list)
    special_requirement: Optional[str] = None


class TimeSlot(BaseModel):
    slot_id: str
    time: str


class Constraint(BaseModel):
    constraint_type: str
    rule: str


class SectionRequirement(BaseModel):
    section: str
    subject: str
    periods_per_week: int
    preferred_pattern: Optional[str] = None


class TimetableRecord(BaseModel):
    day: str
    slot: str
    section: str
    subject: str
    faculty: str
    room: str


class AcademicDocument(BaseModel):
    institution: Institution
    academic: AcademicInfo

    sections: List[Section] = Field(default_factory=list)
    subjects: List[Subject] = Field(default_factory=list)
    faculty: List[Faculty] = Field(default_factory=list)
    rooms: List[Room] = Field(default_factory=list)
    time_slots: List[TimeSlot] = Field(default_factory=list)
    constraints: List[Constraint] = Field(default_factory=list)
    section_requirements: List[SectionRequirement] = Field(default_factory=list)
    timetable_records: List[TimetableRecord] = Field(default_factory=list)