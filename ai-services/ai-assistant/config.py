"""Configuration settings for AI Assistant."""
import os
from pydantic import BaseModel, Field

class Settings(BaseModel):
    app_name: str = "IntelliSchedule AI Assistant"
    gemini_api_key: str = Field(default=os.getenv("GEMINI_API_KEY", ""))
    gemini_model: str = Field(default=os.getenv("GEMINI_MODEL", "gemini-1.5-flash"))
    timetable_service_url: str = Field(default=os.getenv("TIMETABLE_SERVICE_URL", "http://localhost:8083"))

settings = Settings()
