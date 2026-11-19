"""Unit test for system prompt builder."""
from services.prompt_builder import SystemPromptBuilder

def test_prompt_builder_contains_role():
    prompt = SystemPromptBuilder.build_system_prompt([], "faculty")
    assert "faculty" in prompt
    assert "IntelliSchedule" in prompt
