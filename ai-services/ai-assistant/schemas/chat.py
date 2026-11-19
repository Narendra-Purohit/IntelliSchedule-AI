"""Schemas for chat interactions."""
from pydantic import BaseModel
from typing import Optional, List, Dict, Any

class ChatRequest(BaseModel):
    message: str
    user_role: Optional[str] = "student"
    context_filters: Optional[Dict[str, str]] = None

class ChatResponse(BaseModel):
    reply: str
    suggested_actions: Optional[List[str]] = None
