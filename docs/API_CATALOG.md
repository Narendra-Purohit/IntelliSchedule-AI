# API Endpoint Catalog

## 1. Authentication (`/api/v1/auth`)
- `POST /register`: Register user account
- `POST /login`: Generate JWT access token

## 2. Timetable & Academic Entities (`/api/v1/timetables`)
- `POST /generate`: Execute timetable constraint solver
- `GET /`: Retrieve all generated timetables
- `GET /{id}/conflicts`: Audit schedule conflicts

## 3. Document Intelligence (`/api/v1/documents`)
- `POST /upload`: Upload syllabus PDF for structured extraction

## 4. Reports (`/api/v1/reports`)
- `GET /timetable/{id}/pdf`: Download styled PDF timetable
- `GET /timetable/{id}/excel`: Download Excel spreadsheet
