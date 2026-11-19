# Changelog — IntelliSchedule-AI

All notable technical deliverables implemented in this autonomous cycle:

## [1.0.0] - 2026-11-19
### Added
- **Document Intelligence Hardening**: Magic bytes verification, max size checks, custom exception hierarchy, and pytest coverage.
- **Microservices DevOps**: Multi-stage Dockerfiles for Gateway, Auth, Timetable, Report, and Doc-Intel services, plus root `docker-compose.yml`.
- **Full Report Service (`backend/services/report-service`)**: OpenPDF timetable/faculty layout generation, Apache POI Excel export sheets, and REST endpoints.
- **AI Ingestion Bridge**: Bidirectional mapping between FastAPI extraction schemas and Spring Boot entities (`Subject`, `Faculty`).
- **Scheduling Constraint Hardening**: Constraint validators (`FacultyDoubleBookingConstraint`, `RoomCollisionConstraint`), fitness score calculation, and conflict audit endpoint.
- **AI Assistant Scaffolding**: FastAPI query service with Gemini context builder and chat endpoints.
- **Client Frontend**: Web dashboard for schedule visualization, syllabus uploading, and report downloading.
