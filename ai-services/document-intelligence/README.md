# Document Intelligence Service

AI-powered academic document ingestion and extraction microservice for **IntelliSchedule-AI**.

## Key Capabilities
- **Dual-Engine Text Extraction**: Fast native extraction via PyMuPDF with Tesseract OCR fallback for scanned materials.
- **Academic Schema Structuring**: Powered by Gemini API to parse subjects, weekly credits, and faculty allocations into typed Pydantic models.
- **Fail-Safe Processing**: Comprehensive PDF header verification, magic bytes check, and structured error responses.

## API Endpoints
- `GET /api/v1/health`: Service health indicator.
- `POST /api/v1/documents/upload`: Multipart file upload accepting valid PDF documents.

## Local Execution
```bash
pip install -r requirements.txt
uvicorn main:app --port 8000 --reload
```
