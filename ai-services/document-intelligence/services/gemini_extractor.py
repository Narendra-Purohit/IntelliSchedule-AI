import os
import time

from dotenv import load_dotenv
from google import genai
from google.genai import types

from schemas.academic import AcademicDocument


load_dotenv()


class GeminiExtractor:

    def __init__(self):

        # -----------------------------------------
        # Load Gemini API configuration
        # -----------------------------------------

        api_key = os.getenv("GEMINI_API_KEY")

        if not api_key:
            raise ValueError(
                "GEMINI_API_KEY is not configured"
            )

        self.model = os.getenv(
            "GEMINI_MODEL",
            "gemini-2.5-flash"
        )

        self.client = genai.Client(
            api_key=api_key
        )

        print(
            f"GeminiExtractor initialized "
            f"with model: {self.model}"
        )

    def extract(
        self,
        document_text: str
    ) -> AcademicDocument:

        print("GEMINI EXTRACTION STARTED")

        # -----------------------------------------
        # Validate input
        # -----------------------------------------

        if not document_text or not document_text.strip():
            raise ValueError(
                "Document text is empty"
            )

        # -----------------------------------------
        # Prompt
        # -----------------------------------------

        prompt = f"""
You are the Document Intelligence component
of IntelliSchedule AI.

Your job is to extract academic scheduling
information from the provided document.

IMPORTANT RULES:

1. Extract ONLY information explicitly present
   in the document.

2. Do NOT invent information.

3. Do NOT guess missing values.

4. Do NOT modify or assume values.

5. If information is missing, use null where
   allowed or an empty list.

6. Return data according to the
   AcademicDocument schema.

Extract the following information whenever
present:

- Institution information
- Department
- Academic program
- Semester
- Academic year
- Sections
- Student strength
- Class coordinators
- Subjects
- Subject codes
- Subject type
- Credits
- Periods per week
- Faculty
- Faculty availability
- Faculty unavailable slots
- Rooms
- Room type
- Room capacity
- Room availability
- Special room requirements
- Time slots
- Hard constraints
- Soft constraints
- Section-specific requirements
- Existing timetable records

The extracted data will be used by an
Intelligent Timetable Generation System.

DOCUMENT:

{document_text}
"""

        # -----------------------------------------
        # Retry configuration
        # -----------------------------------------

        max_retries = 3
        base_delay = 2

        # -----------------------------------------
        # Gemini request
        # -----------------------------------------

        for attempt in range(1, max_retries + 1):

            try:

                print(
                    f"Gemini request attempt "
                    f"{attempt}/{max_retries}"
                )

                response = self.client.models.generate_content(
                    model=self.model,
                    contents=prompt,
                    config=types.GenerateContentConfig(
                        response_mime_type="application/json",
                        response_schema=AcademicDocument,
                    ),
                )

                print("GEMINI RESPONSE RECEIVED")

                # ---------------------------------
                # Validate Gemini response
                # ---------------------------------

                if not response.text:
                    raise ValueError(
                        "Gemini returned an empty response"
                    )

                academic_document = (
                    AcademicDocument.model_validate_json(
                        response.text
                    )
                )

                print(
                    "GEMINI RESPONSE VALIDATED "
                    "SUCCESSFULLY"
                )

                return academic_document

            except Exception as e:

                error_message = str(e)

                print(
                    f"Gemini request failed: "
                    f"{error_message}"
                )

                # ---------------------------------
                # Retry temporary errors
                # ---------------------------------

                temporary_error = (
                    "503" in error_message
                    or "UNAVAILABLE" in error_message
                    or "500" in error_message
                    or "429" in error_message
                )

                if not temporary_error:
                    raise

                # ---------------------------------
                # Final attempt failed
                # ---------------------------------

                if attempt == max_retries:

                    raise RuntimeError(
                        "Gemini service is temporarily "
                        "unavailable after multiple "
                        "retry attempts."
                    ) from e

                # ---------------------------------
                # Exponential backoff
                # ---------------------------------

                delay = base_delay * (
                    2 ** (attempt - 1)
                )

                print(
                    f"Retrying Gemini request "
                    f"in {delay} seconds..."
                )

                time.sleep(delay)

        raise RuntimeError(
            "Gemini extraction failed"
        )