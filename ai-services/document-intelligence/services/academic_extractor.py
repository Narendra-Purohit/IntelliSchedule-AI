import re
from typing import List

from schemas.academic import (
    AcademicDocument,
    AcademicInfo,
    Constraint,
    Faculty,
    Institution,
    Room,
    Section,
    SectionRequirement,
    Subject,
    TimeSlot,
    TimetableRecord,
)


class AcademicExtractor:
    """
    Extract structured academic information from raw PDF text.

    This is the first deterministic extraction layer.
    Gemini will be integrated later for more flexible document understanding.
    """

    def extract(self, raw_text: str) -> AcademicDocument:

        text = self._clean_text(raw_text)

        institution = self._extract_institution(text)
        academic = self._extract_academic_info(text)
        sections = self._extract_sections(text)
        subjects = self._extract_subjects(text)
        faculty = self._extract_faculty(text)
        rooms = self._extract_rooms(text)
        time_slots = self._extract_time_slots(text)
        constraints = self._extract_constraints(text)
        section_requirements = self._extract_section_requirements(text)
        timetable_records = self._extract_timetable_records(text)

        return AcademicDocument(
            institution=institution,
            academic=academic,
            sections=sections,
            subjects=subjects,
            faculty=faculty,
            rooms=rooms,
            time_slots=time_slots,
            constraints=constraints,
            section_requirements=section_requirements,
            timetable_records=timetable_records,
        )

    # ---------------------------------------------------------
    # CLEANING
    # ---------------------------------------------------------

    def _clean_text(self, text: str) -> str:
        """
        Normalize extracted PDF text without destroying
        important line-based information.
        """

        text = text.replace("\r\n", "\n")
        text = text.replace("\r", "\n")

        # Remove excessive spaces while keeping newlines.
        text = re.sub(r"[ \t]+", " ", text)

        # Remove excessive blank lines.
        text = re.sub(r"\n{3,}", "\n\n", text)

        return text.strip()

    # ---------------------------------------------------------
    # INSTITUTION
    # ---------------------------------------------------------

    def _extract_institution(self, text: str) -> Institution:

        institution_match = re.search(
            r"Institution\s*\n\s*([^\n]+)",
            text,
            re.IGNORECASE,
        )

        department_match = re.search(
            r"Department\s*\n\s*([^\n]+)",
            text,
            re.IGNORECASE,
        )

        return Institution(
            name=(
                institution_match.group(1).strip()
                if institution_match
                else "Unknown"
            ),
            department=(
                department_match.group(1).strip()
                if department_match
                else None
            ),
        )

    # ---------------------------------------------------------
    # ACADEMIC INFORMATION
    # ---------------------------------------------------------

    def _extract_academic_info(self, text: str) -> AcademicInfo:

        program_match = re.search(
            r"Program\s*\n\s*([^\n]+)",
            text,
            re.IGNORECASE,
        )

        year_match = re.search(
            r"Academic Year\s*\n\s*([^\n]+)",
            text,
            re.IGNORECASE,
        )

        semester_match = re.search(
            r"Semester\s*\n\s*([^\n]+)",
            text,
            re.IGNORECASE,
        )

        return AcademicInfo(
            program=(
                program_match.group(1).strip()
                if program_match
                else None
            ),
            semester=(
                semester_match.group(1).strip()
                if semester_match
                else None
            ),
            academic_year=(
                year_match.group(1).strip()
                if year_match
                else None
            ),
        )

    # ---------------------------------------------------------
    # SECTIONS
    # ---------------------------------------------------------

    def _extract_sections(self, text: str) -> List[Section]:

        results = []

        pattern = re.compile(
            r"\b(MCA)\s*\n"
            r"(III)\s*\n"
            r"([A-Z])\s*\n"
            r"(\d+)\s*\n"
            r"([^\n]+)",
            re.IGNORECASE,
        )

        for match in pattern.finditer(text):

            results.append(
                Section(
                    course=match.group(1).strip(),
                    semester=match.group(2).strip(),
                    section=match.group(3).strip(),
                    students=int(match.group(4)),
                    class_coordinator=match.group(5).strip(),
                )
            )

        return results

    # ---------------------------------------------------------
    # SUBJECTS
    # ---------------------------------------------------------

    def _extract_subjects(self, text: str) -> List[Subject]:

        results = []

        subject_pattern = re.compile(
            r"(MCA\d{3})\s*\n"
            r"(.+?)\s*\n"
            r"(Theory|Lab)\s*\n"
            r"(\d+)\s*\n"
            r"(\d+)\s*\n"
            r"([^\n]+)\s*\n"
            r"(Classroom|Computer Lab|AI Lab)",
            re.IGNORECASE | re.DOTALL,
        )

        for match in subject_pattern.finditer(text):

            subject_name = " ".join(
                match.group(2).split()
            )

            results.append(
                Subject(
                    code=match.group(1).strip(),
                    name=subject_name,
                    type=match.group(3).strip(),
                    credits=int(match.group(4)),
                    periods_per_week=int(match.group(5)),
                    faculty=match.group(6).strip(),
                    room_type=match.group(7).strip(),
                )
            )

        return results

    # ---------------------------------------------------------
    # FACULTY
    # ---------------------------------------------------------

    def _extract_faculty(self, text: str) -> List[Faculty]:

        results = []

        pattern = re.compile(
            r"(F\d{3})\s*\n"
            r"([^\n]+)\s*\n"
            r"([^\n]+)\s*\n"
            r"([^\n]+)\s*\n"
            r"([^\n]+)",
            re.IGNORECASE,
        )

        faculty_section = self._get_section(
            text,
            "4. Faculty Information and Availability",
            "5. Classroom and Laboratory Resources",
        )

        for match in pattern.finditer(faculty_section):

            available_days = [
                day.strip()
                for day in match.group(4).split(",")
            ]

            results.append(
                Faculty(
                    faculty_id=match.group(1).strip(),
                    name=match.group(2).strip(),
                    department=match.group(3).strip(),
                    available_days=available_days,
                    unavailable_slots=[
                        match.group(5).strip()
                    ],
                )
            )

        return results

    # ---------------------------------------------------------
    # ROOMS
    # ---------------------------------------------------------

    def _extract_rooms(self, text: str) -> List[Room]:

        results = []

        room_section = self._get_section(
            text,
            "5. Classroom and Laboratory Resources",
            "6. Time Slots",
        )

        pattern = re.compile(
            r"(R\d{3}|LAB\d+)\s*\n"
            r"([^\n]+)\s*\n"
            r"(Classroom|Computer Lab|AI Lab)\s*\n"
            r"(\d+)\s*\n"
            r"([^\n]+)\s*\n"
            r"([^\n]+)",
            re.IGNORECASE,
        )

        for match in pattern.finditer(room_section):

            results.append(
                Room(
                    room_id=match.group(1).strip(),
                    name=match.group(2).strip(),
                    type=match.group(3).strip(),
                    capacity=int(match.group(4)),
                    available_days=[
                        match.group(5).strip()
                    ],
                    special_requirement=match.group(6).strip(),
                )
            )

        return results

    # ---------------------------------------------------------
    # TIME SLOTS
    # ---------------------------------------------------------

    def _extract_time_slots(self, text: str) -> List[TimeSlot]:

        results = []

        section = self._get_section(
            text,
            "6. Time Slots",
            "7. Scheduling Constraints",
        )

        pattern = re.compile(
            r"(S\d+)\s*\n"
            r"(\d{2}:\d{2}\s*-\s*\d{2}:\d{2})"
        )

        for match in pattern.finditer(section):

            results.append(
                TimeSlot(
                    slot_id=match.group(1).strip(),
                    time=match.group(2).strip(),
                )
            )

        return results

    # ---------------------------------------------------------
    # CONSTRAINTS
    # ---------------------------------------------------------

    def _extract_constraints(self, text: str) -> List[Constraint]:

        results = []

        section = self._get_section(
            text,
            "7. Scheduling Constraints",
            "8. Section-Specific Requirements",
        )

        pattern = re.compile(
            r"\b(Hard|Soft)\s*\n"
            r"([^\n]+)"
        )

        for match in pattern.finditer(section):

            results.append(
                Constraint(
                    constraint_type=match.group(1).strip(),
                    rule=match.group(2).strip(),
                )
            )

        return results

    # ---------------------------------------------------------
    # SECTION REQUIREMENTS
    # ---------------------------------------------------------

    def _extract_section_requirements(
        self,
        text: str,
    ) -> List[SectionRequirement]:

        results = []

        section = self._get_section(
            text,
            "8. Section-Specific Requirements",
            "9. Sample Timetable Records",
        )

        pattern = re.compile(
            r"([A-Z])\s*\n"
            r"(.+?)\s*\n"
            r"(\d+)\s*\n"
            r"([^\n]+)",
            re.IGNORECASE | re.DOTALL,
        )

        for match in pattern.finditer(section):

            subject = " ".join(
                match.group(2).split()
            )

            results.append(
                SectionRequirement(
                    section=match.group(1).strip(),
                    subject=subject,
                    periods_per_week=int(match.group(3)),
                    preferred_pattern=match.group(4).strip(),
                )
            )

        return results

    # ---------------------------------------------------------
    # TIMETABLE RECORDS
    # ---------------------------------------------------------

    def _extract_timetable_records(
        self,
        text: str,
    ) -> List[TimetableRecord]:

        results = []

        section = self._get_section(
            text,
            "9. Sample Timetable Records",
            "10. AI Extraction Target",
        )

        pattern = re.compile(
            r"(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)\s*\n"
            r"(S\d+(?:-S\d+)?)\s*\n"
            r"([A-Z])\s*\n"
            r"(MCA\d{3}\s+.+?)\s*\n"
            r"(Dr\.[^\n]+|Prof\.[^\n]+)\s*\n"
            r"(R\d{3}|LAB\d+)",
            re.IGNORECASE | re.DOTALL,
        )

        for match in pattern.finditer(section):

            subject = " ".join(
                match.group(4).split()
            )

            results.append(
                TimetableRecord(
                    day=match.group(1).strip(),
                    slot=match.group(2).strip(),
                    section=match.group(3).strip(),
                    subject=subject,
                    faculty=match.group(5).strip(),
                    room=match.group(6).strip(),
                )
            )

        return results

    # ---------------------------------------------------------
    # SECTION HELPER
    # ---------------------------------------------------------

    def _get_section(
        self,
        text: str,
        start_heading: str,
        end_heading: str,
    ) -> str:

        start_index = text.find(start_heading)

        if start_index == -1:
            return ""

        start_index += len(start_heading)

        end_index = text.find(
            end_heading,
            start_index,
        )

        if end_index == -1:
            return text[start_index:]

        return text[start_index:end_index]