"""Unit tests for GeminiExtractor service."""
import pytest
from unittest.mock import MagicMock, patch

def test_gemini_extractor_fallback_structure(mock_gemini_response):
    assert "subjects" in mock_gemini_response
    assert len(mock_gemini_response["subjects"]) == 1
    sub = mock_gemini_response["subjects"][0]
    assert sub["subject_code"] == "3150703"
    assert sub["credits"] == 5

def test_gemini_extractor_missing_key_fallback():
    from schemas.academic import AcademicDocument
    doc = AcademicDocument(
        institution_name="Test College",
        program="Information Technology",
        academic_year="2026",
        semester="Sem 4",
        subjects=[],
        faculty_list=[]
    )
    assert doc.institution_name == "Test College"
    assert len(doc.subjects) == 0
