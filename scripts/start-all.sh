#!/usr/bin/env bash
echo "Launching IntelliSchedule-AI Services..."

# Document Intelligence
(cd ai-services/document-intelligence && uvicorn main:app --port 8000 --reload) &

# Backend services
for s in api-gateway auth-service timetable-service report-service; do
    (cd backend/services/$s && ./mvnw spring-boot:run) &
done

echo "Services started in background."
