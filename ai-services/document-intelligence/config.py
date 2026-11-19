"""Application configuration and settings."""
import os
from pydantic import BaseModel, Field

class Settings(BaseModel):
    app_name: str = "IntelliSchedule Document Intelligence"
    app_version: str = "1.0.0"
    max_file_size_bytes: int = Field(default=15 * 1024 * 1024)  # 15 MB
    ocr_min_char_threshold: int = Field(default=50)
    gemini_model_name: str = Field(default="gemini-1.5-flash")
    gemini_api_key: str = Field(default=os.getenv("GEMINI_API_KEY", ""))
    cors_allowed_origins: list[str] = Field(
        default=["http://localhost:8080", "http://localhost:5500", "http://127.0.0.1:5500"]
    )

settings = Settings()
