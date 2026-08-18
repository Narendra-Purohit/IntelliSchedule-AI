from fastapi import FastAPI, UploadFile, File, HTTPException
import fitz

from services.ocr_service import OCRService
from services.gemini_extractor import GeminiExtractor


app = FastAPI(
    title="IntelliSchedule Document Intelligence",
    description="AI-powered academic document processing service",
    version="1.0.0"
)


ocr_service = OCRService()
gemini_extractor = GeminiExtractor()


@app.get("/api/v1/health")
async def health_check():
    return {
        "service": "document-intelligence",
        "status": "UP"
    }


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

    try:

        # -----------------------------------------
        # STEP 1: Read PDF using PyMuPDF
        # -----------------------------------------

        pdf = fitz.open(
            stream=file_bytes,
            filetype="pdf"
        )

        pages = []

        for page_number, page in enumerate(
            pdf,
            start=1
        ):

            text = page.get_text("text")

            pages.append({
                "page_number": page_number,
                "text": text.strip()
            })

        page_count = len(pdf)

        pdf.close()

        # -----------------------------------------
        # STEP 2: Combine PDF text
        # -----------------------------------------

        raw_text = "\n".join(
            page["text"]
            for page in pages
        ).strip()

        # -----------------------------------------
        # STEP 3: OCR FALLBACK
        # -----------------------------------------

        ocr_used = False

        if len(raw_text) < 50:

            raw_text = (
                ocr_service
                .extract_text_from_pdf(file_bytes)
            )

            ocr_used = True

        # -----------------------------------------
        # STEP 4: Gemini Extraction
        # -----------------------------------------

        academic_document = (
            gemini_extractor.extract(raw_text)
        )

        # -----------------------------------------
        # STEP 5: Response
        # -----------------------------------------

        return {
            "success": True,
            "filename": file.filename,
            "content_type": file.content_type,
            "page_count": page_count,
            "extraction_method": (
                "ocr"
                if ocr_used
                else "pymupdf"
            ),
            "data": academic_document.model_dump()
        }

    except Exception as e:

        raise HTTPException(
            status_code=400,
            detail=f"Unable to process PDF: {str(e)}"
        )