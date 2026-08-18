import io

import pymupdf
import pytesseract

from PIL import Image


class OCRService:

    def extract_text_from_pdf(
        self,
        file_bytes: bytes
    ) -> str:
        """
        Extract text from a scanned/image-based PDF
        using Tesseract OCR.
        """

        pdf = pymupdf.open(
            stream=file_bytes,
            filetype="pdf"
        )

        extracted_pages = []

        for page_number, page in enumerate(
            pdf,
            start=1
        ):

            # Convert PDF page into high-resolution image
            pixmap = page.get_pixmap(
                matrix=pymupdf.Matrix(2, 2)
            )

            image_bytes = pixmap.tobytes(
                "png"
            )

            image = Image.open(
                io.BytesIO(image_bytes)
            )

            # OCR
            text = pytesseract.image_to_string(
                image
            )

            if text.strip():

                extracted_pages.append(
                    f"--- Page {page_number} ---\n"
                    f"{text.strip()}"
                )

        pdf.close()

        return "\n\n".join(
            extracted_pages
        )