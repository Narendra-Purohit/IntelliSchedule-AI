# AI Assistant Service

Conversational natural-language interface for **IntelliSchedule-AI**.

## Endpoints
- `GET /api/v1/health`: Health status.
- `POST /api/v1/chat`: Ask questions about schedule, faculty allocations, and room availability.

## Running Locally
```bash
pip install -r requirements.txt
uvicorn main:app --port 8001 --reload
```
