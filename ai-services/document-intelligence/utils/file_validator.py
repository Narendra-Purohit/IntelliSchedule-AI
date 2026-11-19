"""File validation helpers for PDF verification."""
from exceptions import CorruptedFileException, UnsupportedMediaTypeException

PDF_MAGIC_BYTES = b"%PDF-"

def validate_pdf_bytes(file_bytes: bytes, filename: str = "") -> None:
    """Validates that file content begins with standard PDF header bytes."""
    if not file_bytes:
        raise CorruptedFileException("Uploaded file is empty")
    
    if len(file_bytes) < len(PDF_MAGIC_BYTES):
        raise CorruptedFileException("Uploaded file header is truncated")
    
    if not file_bytes.startswith(PDF_MAGIC_BYTES):
        raise UnsupportedMediaTypeException(
            f"File '{filename}' does not contain valid PDF magic bytes"
        )
