"""Gemini chat orchestrator service."""
from services.timetable_client import TimetableQueryClient
from services.prompt_builder import SystemPromptBuilder
from config import settings

class GeminiChatService:
    def __init__(self):
        self.client = TimetableQueryClient()

    async def answer_query(self, user_message: str, user_role: str = "student") -> str:
        schedule = await self.client.get_schedule()
        prompt = SystemPromptBuilder.build_system_prompt(schedule, user_role)
        # Mock / baseline response generator
        return f"Based on your timetable: classes are scheduled according to academic hours. Details for '{user_message}' have been retrieved."
