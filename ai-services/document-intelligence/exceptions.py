"""Custom exceptions for Document Intelligence service."""

class DocumentIntelligenceException(Exception):
    """Base exception for all document intelligence errors."""
    def __init__(self, message: str, status_code: int = 500):
        super().__init__(message)
        self.message = message
        self.status_code = status_code


class CorruptedFileException(DocumentIntelligenceException):
    def __init__(self, message: str = "Invalid or corrupted PDF file"):
        super().__init__(message, status_code=400)


class FileSizeLimitExceededException(DocumentIntelligenceException):
    def __init__(self, max_mb: int = 15):
        super().__init__(f"File size exceeds maximum allowed limit of {max_mb}MB", status_code=413)


class UnsupportedMediaTypeException(DocumentIntelligenceException):
    def __init__(self, message: str = "Only valid application/pdf files are supported"):
        super().__init__(message, status_code=415)


class OCRExtractionException(DocumentIntelligenceException):
    def __init__(self, detail: str = "OCR processing failed"):
        super().__init__(f"OCR Extraction failure: {detail}", status_code=502)


class GeminiExtractionException(DocumentIntelligenceException):
    def __init__(self, detail: str = "AI model failed to extract academic structure"):
        super().__init__(f"Model extraction failure: {detail}", status_code=502)
