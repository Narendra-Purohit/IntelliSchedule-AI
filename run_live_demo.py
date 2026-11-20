import io
import os
import sys
import uvicorn
from fastapi import FastAPI, UploadFile, File, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import Response, FileResponse, HTMLResponse
from fastapi.staticfiles import StaticFiles
import pymupdf
from reportlab.lib.pagesizes import letter, landscape
from reportlab.lib import colors
from reportlab.platypus import SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
import openpyxl
from openpyxl.styles import Font, PatternFill, Alignment, Border, Side

app = FastAPI(
    title="IntelliSchedule-AI Live Platform",
    description="Live unified backend and dashboard server for college demonstration",
    version="2.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# In-memory database of active schedules
schedules_db = [
    {
        "id": 1,
        "dayOfWeek": "Monday",
        "timeSlot": {"startTime": "09:00", "endTime": "10:00"},
        "subject": {"name": "Data Structures & Algorithms", "code": "CS301"},
        "faculty": {"name": "Dr. Alan Turing"},
        "room": {"roomNumber": "LH-101"},
        "section": {"name": "Batch-A"}
    },
    {
        "id": 2,
        "dayOfWeek": "Monday",
        "timeSlot": {"startTime": "10:00", "endTime": "11:00"},
        "subject": {"name": "Database Management Systems", "code": "CS302"},
        "faculty": {"name": "Prof. Ada Lovelace"},
        "room": {"roomNumber": "LH-102"},
        "section": {"name": "Batch-A"}
    },
    {
        "id": 3,
        "dayOfWeek": "Tuesday",
        "timeSlot": {"startTime": "09:00", "endTime": "10:00"},
        "subject": {"name": "Operating Systems", "code": "CS303"},
        "faculty": {"name": "Dr. Linus Torvalds"},
        "room": {"roomNumber": "LH-101"},
        "section": {"name": "Batch-A"}
    },
    {
        "id": 4,
        "dayOfWeek": "Tuesday",
        "timeSlot": {"startTime": "11:15", "endTime": "12:15"},
        "subject": {"name": "Computer Networks", "code": "CS304"},
        "faculty": {"name": "Prof. Claude Shannon"},
        "room": {"roomNumber": "LH-103"},
        "section": {"name": "Batch-A"}
    },
    {
        "id": 5,
        "dayOfWeek": "Wednesday",
        "timeSlot": {"startTime": "09:00", "endTime": "11:00"},
        "subject": {"name": "Advanced Python Lab", "code": "CS305P"},
        "faculty": {"name": "Prof. Guido van Rossum"},
        "room": {"roomNumber": "Lab-3"},
        "section": {"name": "Batch-A"}
    },
    {
        "id": 6,
        "dayOfWeek": "Thursday",
        "timeSlot": {"startTime": "10:00", "endTime": "11:00"},
        "subject": {"name": "Software Engineering", "code": "CS306"},
        "faculty": {"name": "Dr. Margaret Hamilton"},
        "room": {"roomNumber": "LH-102"},
        "section": {"name": "Batch-A"}
    },
    {
        "id": 7,
        "dayOfWeek": "Friday",
        "timeSlot": {"startTime": "09:00", "endTime": "10:00"},
        "subject": {"name": "Artificial Intelligence & ML", "code": "CS307"},
        "faculty": {"name": "Prof. Geoffrey Hinton"},
        "room": {"roomNumber": "LH-101"},
        "section": {"name": "Batch-A"}
    },
    {
        "id": 8,
        "dayOfWeek": "Friday",
        "timeSlot": {"startTime": "10:00", "endTime": "11:00"},
        "subject": {"name": "Web Technologies", "code": "CS308"},
        "faculty": {"name": "Dr. Tim Berners-Lee"},
        "room": {"roomNumber": "LH-104"},
        "section": {"name": "Batch-A"}
    }
]

# Health check
@app.get("/api/v1/health")
async def health():
    return {"status": "UP", "service": "intellischedule-live", "mode": "demo"}

# Timetable endpoints
@app.get("/api/v1/timetables")
async def get_timetables():
    return schedules_db

@app.post("/api/v1/timetables/generate")
async def generate_timetables(programId: int = None, semesterId: int = None):
    # Simulated heuristic constraint optimization pass
    return schedules_db

# Document intelligence upload
@app.post("/api/v1/documents/upload")
async def upload_document(file: UploadFile = File(...)):
    if not file.filename.endswith(".pdf"):
        raise HTTPException(status_code=400, detail="Only PDF files are allowed")
    
    file_bytes = await file.read()
    if not file_bytes.startswith(b"%PDF-"):
        raise HTTPException(status_code=400, detail="File does not contain valid PDF magic bytes")

    try:
        pdf = pymupdf.open(stream=file_bytes, filetype="pdf")
        page_count = len(pdf)
        if page_count > 20:
            pdf.close()
            raise HTTPException(status_code=422, detail="Document exceeds maximum syllabus limit of 20 pages")
        
        extracted_text = ""
        for page in pdf:
            extracted_text += page.get_text("text") + "\\n"
        pdf.close()
    except Exception as e:
        raise HTTPException(status_code=400, detail=f"PDF parsing error: {e}")

    # Simulated structured academic document output
    return {
        "success": True,
        "filename": file.filename,
        "page_count": page_count,
        "extraction_method": "pymupdf",
        "data": {
            "institution_name": "Gujarat Technological University",
            "program": "Computer Engineering",
            "academic_year": "2026-2027",
            "semester": "Semester 5",
            "subjects": [
                {"subject_code": "CS501", "subject_name": "Analysis & Design of Algorithms", "credits": 4},
                {"subject_code": "CS502", "subject_name": "Operating Systems", "credits": 4},
                {"subject_code": "CS503", "subject_name": "Computer Networks", "credits": 3},
                {"subject_code": "CS504", "subject_name": "Cyber Security", "credits": 3}
            ],
            "faculty_list": [
                {"faculty_name": "Dr. Alan Turing", "department": "Computer Science"},
                {"faculty_name": "Prof. Ada Lovelace", "department": "Computer Science"}
            ]
        }
    }

# PDF Export Report Generation
@app.get("/api/v1/reports/timetable/pdf")
async def download_pdf():
    buffer = io.BytesIO()
    doc = SimpleDocTemplate(buffer, pagesize=landscape(letter), rightMargin=30, leftMargin=30, topMargin=30, bottomMargin=30)
    styles = getSampleStyleSheet()
    elements = []

    title_style = ParagraphStyle(
        'DocTitle',
        parent=styles['Heading1'],
        fontSize=18,
        textColor=colors.HexColor("#1e3a8a"),
        alignment=1, # Center
        spaceAfter=10
    )
    elements.append(Paragraph("IntelliSchedule-AI — Master Weekly Timetable", title_style))
    elements.append(Paragraph("Academic Schedule (Batch A - Computer Engineering)", styles['Normal']))
    elements.append(Spacer(1, 15))

    table_data = [["Day", "Time Slot", "Subject", "Code", "Faculty", "Room", "Section"]]
    for s in schedules_db:
        table_data.append([
            s["dayOfWeek"],
            f"{s['timeSlot']['startTime']} - {s['timeSlot']['endTime']}",
            s["subject"]["name"],
            s["subject"]["code"],
            s["faculty"]["name"],
            s["room"]["roomNumber"],
            s["section"]["name"]
        ])

    table = Table(table_data, colWidths=[80, 95, 180, 65, 130, 65, 75])
    table.setStyle(TableStyle([
        ('BACKGROUND', (0, 0), (-1, 0), colors.HexColor("#1e3a8a")),
        ('TEXTCOLOR', (0, 0), (-1, 0), colors.white),
        ('ALIGN', (0, 0), (-1, -1), 'CENTER'),
        ('FONTNAME', (0, 0), (-1, 0), 'Helvetica-Bold'),
        ('FONTSIZE', (0, 0), (-1, 0), 10),
        ('BOTTOMPADDING', (0, 0), (-1, 0), 8),
        ('GRID', (0, 0), (-1, -1), 0.5, colors.HexColor("#cbd5e1")),
        ('ROWBACKGROUNDS', (0, 1), (-1, -1), [colors.white, colors.HexColor("#f8fafc")]),
        ('FONTSIZE', (0, 1), (-1, -1), 9),
    ]))
    elements.append(table)

    doc.build(elements)
    buffer.seek(0)
    return Response(
        content=buffer.getvalue(),
        media_type="application/pdf",
        headers={"Content-Disposition": "attachment; filename=timetable_schedule.pdf"}
    )

# Excel Export Report Generation
@app.get("/api/v1/reports/timetable/excel")
async def download_excel():
    wb = openpyxl.Workbook()
    ws = wb.active
    ws.title = "Weekly Timetable"

    header_font = Font(name="Calibri", size=11, bold=True, color="FFFFFF")
    header_fill = PatternFill(start_color="1E3A8A", end_color="1E3A8A", fill_type="solid")
    border_style = Border(
        left=Side(style='thin', color='CBD5E1'),
        right=Side(style='thin', color='CBD5E1'),
        top=Side(style='thin', color='CBD5E1'),
        bottom=Side(style='thin', color='CBD5E1')
    )

    columns = ["Day", "Time Slot", "Subject", "Code", "Faculty", "Room", "Section"]
    ws.append(columns)
    for col in range(1, len(columns) + 1):
        cell = ws.cell(row=1, column=col)
        cell.font = header_font
        cell.fill = header_fill
        cell.alignment = Alignment(horizontal="center", vertical="center")

    for s in schedules_db:
        ws.append([
            s["dayOfWeek"],
            f"{s['timeSlot']['startTime']} - {s['timeSlot']['endTime']}",
            s["subject"]["name"],
            s["subject"]["code"],
            s["faculty"]["name"],
            s["room"]["roomNumber"],
            s["section"]["name"]
        ])

    for row in ws.iter_rows(min_row=2, max_row=len(schedules_db) + 1, min_col=1, max_col=len(columns)):
        for cell in row:
            cell.border = border_style
            cell.alignment = Alignment(vertical="center")

    for col in ws.columns:
        max_len = max(len(str(cell.value or '')) for cell in col)
        col_letter = openpyxl.utils.get_column_letter(col[0].column)
        ws.column_dimensions[col_letter].width = max(max_len + 4, 12)

    buffer = io.BytesIO()
    wb.save(buffer)
    buffer.seek(0)
    return Response(
        content=buffer.getvalue(),
        media_type="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        headers={"Content-Disposition": "attachment; filename=timetable_schedule.xlsx"}
    )

# Static frontend mount
frontend_dir = os.path.join(os.path.dirname(__file__), "frontend")
if os.path.exists(frontend_dir):
    app.mount("/", StaticFiles(directory=frontend_dir, html=True), name="frontend")

if __name__ == "__main__":
    print("\n=======================================================")
    print("🚀 IntelliSchedule-AI Live Platform Running!")
    print("👉 Open your browser at: http://localhost:8080")
    print("=======================================================\n")
    uvicorn.run(app, host="127.0.0.1", port=8080)
