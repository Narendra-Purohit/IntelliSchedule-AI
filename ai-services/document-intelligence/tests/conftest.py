"""Pytest fixtures for Document Intelligence service."""
import pytest
from unittest.mock import MagicMock

@pytest.fixture
def sample_pdf_bytes():
    return b"%PDF-1.4\n%\xe2\xe3\xcf\xd3\n1 0 obj\n<<\n/Type /Catalog\n>>\nendobj\ntrailer\n<<\n/Root 1 0 R\n>>\n%%EOF"

@pytest.fixture
def mock_gemini_response():
    return {
        "institution_name": "Gujarat Technological University",
        "academic_year": "2026-2027",
        "semester": "Semester 5",
        "program": "Computer Engineering",
        "subjects": [
            {
                "subject_code": "3150703",
                "subject_name": "Analysis and Design of Algorithms",
                "theory_hours_per_week": 4,
                "practical_hours_per_week": 2,
                "credits": 5,
                "preferred_room_type": "Classroom"
            }
        ],
        "faculty_list": [
            {
                "faculty_name": "Prof. Sharma",
                "department": "Computer Engineering",
                "max_hours_per_week": 16,
                "designated_subjects": ["Analysis and Design of Algorithms"]
            }
        ]
    }
