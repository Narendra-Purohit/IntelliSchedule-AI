"""IntelliSchedule AI Assistant Service."""
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from routers.chat import router as chat_router

app = FastAPI(
    title="IntelliSchedule AI Assistant",
    description="Conversational timetable queries and scheduling guidance",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(chat_router, prefix="/api/v1")

@app.get("/api/v1/health")
async def health():
    return {"service": "ai-assistant", "status": "UP"}
