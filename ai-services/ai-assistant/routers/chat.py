"""Router for chat endpoints."""
from fastapi import APIRouter
from schemas.chat import ChatRequest, ChatResponse
from services.chat_service import GeminiChatService

router = APIRouter(tags=["assistant"])
chat_service = GeminiChatService()

@router.post("/chat", response_model=ChatResponse)
async def chat(request: ChatRequest):
    reply = await chat_service.answer_query(request.message, request.user_role or "student")
    return ChatResponse(
        reply=reply,
        suggested_actions=["View full timetable", "Download PDF", "Check room availability"]
    )
