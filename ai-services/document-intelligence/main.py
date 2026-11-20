import asyncio
from fastapi import FastAPI, UploadFile, File, HTTPException
import pymupdf

from services.ocr_service import OCRService
from services.gemini_extractor import GeminiExtractor
from utils.file_validator import validate_pdf_bytes


app = FastAPI(
    title="IntelliSchedule Document Intelligence",
    description="AI-powered academic document processing service with async workers and guardrails",
    version="1.1.0"
)


ocr_service = OCRService()
gemini_extractor = GeminiExtractor()

MAX_PAGE_LIMIT = 20


@app.get("/api/v1/health")
async def health_check():
    return {
        "service": "document-intelligence",
        "status": "UP"
    }


def _extract_pages_sync(file_bytes: bytes):
    """Synchronous PDF page extraction executed inside worker thread."""
    pdf = pymupdf.open(stream=file_bytes, filetype="pdf")
    page_count = len(pdf)
    
    if page_count > MAX_PAGE_LIMIT:
        pdf.close()
        raise ValueError(f"Uploaded PDF exceeds syllabus limit of {MAX_PAGE_LIMIT} pages (found {page_count} pages)")

    pages = []
    for page_number, page in enumerate(pdf, start=1):
        text = page.get_text("text")
        pages.append({
            "page_number": page_number,
            "text": text.strip()
        })

    pdf.close()
    raw_text = "\n".join(p["text"] for p in pages).strip()
    return page_count, raw_text


@app.post("/api/v1/documents/upload")
async def upload_document(
    file: UploadFile = File(...)
):
    if not file.filename:
        raise HTTPException(
            status_code=400,
            detail="File is required"
        )

    if file.content_type != "application/pdf":
        raise HTTPException(
            status_code=400,
            detail="Only PDF files are allowed"
        )

    file_bytes = await file.read()

    # Step 0: Validate magic bytes
    try:
        validate_pdf_bytes(file_bytes, file.filename)
    except Exception as e:
        raise HTTPException(status_code=400, detail=str(e))

    try:
        # STEP 1 & 2: Read PDF using PyMuPDF in non-blocking worker thread
        try:
            page_count, raw_text = await asyncio.to_thread(_extract_pages_sync, file_bytes)
        except ValueError as ve:
            raise HTTPException(status_code=422, detail=str(ve))

        # STEP 3: OCR FALLBACK (Offloaded to worker thread)
        ocr_used = False
        if len(raw_text) < 50:
            raw_text = await asyncio.to_thread(ocr_service.extract_text_from_pdf, file_bytes)
            ocr_used = True

        # STEP 4: Gemini Extraction (Offloaded to worker thread)
        academic_document = await asyncio.to_thread(gemini_extractor.extract, raw_text)

        # STEP 5: Response
        return {
            "success": True,
            "filename": file.filename,
            "content_type": file.content_type,
            "page_count": page_count,
            "extraction_method": "ocr" if ocr_used else "pymupdf",
            "data": academic_document.model_dump()
        }

    except pymupdf.FileDataError:
        raise HTTPException(
            status_code=400,
            detail="Invalid or corrupted PDF file"
        )

    except HTTPException:
        raise

    except Exception as e:
        err_msg = str(e).lower()
        if "429" in err_msg or "quota" in err_msg or "resourceexhausted" in err_msg:
            raise HTTPException(
                status_code=429,
                detail="AI extraction quota temporarily exceeded. Please try again in 60 seconds."
            )

        raise HTTPException(
            status_code=500,
            detail="Unable to process document at this time"
        )
