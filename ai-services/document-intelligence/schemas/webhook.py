"""Webhook data schemas."""
from typing import Optional, Dict, Any
from pydantic import BaseModel, HttpUrl

class WebhookNotification(BaseModel):
    event_type: str = "document_extracted"
    filename: str
    status: str
    data: Optional[Dict[str, Any]] = None
    error_message: Optional[str] = None
