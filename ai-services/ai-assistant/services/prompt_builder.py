"""Prompt builder for academic timetable queries."""

class SystemPromptBuilder:
    @staticmethod
    def build_system_prompt(schedule_context: list, user_role: str = "student") -> str:
        context_str = f"Available schedule records: {len(schedule_context)} entries."
        return f"""You are the IntelliSchedule Academic Assistant.
Role of user: {user_role}
{context_str}

Help users check class timings, faculty availability, and room locations accurately and politely.
"""
