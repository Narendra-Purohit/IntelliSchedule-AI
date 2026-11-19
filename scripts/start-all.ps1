# IntelliSchedule Local Launcher for Windows
Write-Host "Starting IntelliSchedule-AI Microservices..." -ForegroundColor Cyan

# 1. Document Intelligence
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd ai-services/document-intelligence; uvicorn main:app --port 8000 --reload"

# 2. Backend Services
$services = @("api-gateway", "auth-service", "timetable-service", "report-service")
foreach ($s in $services) {
    Write-Host "Starting $s..." -ForegroundColor Green
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd backend/services/$s; ./mvnw spring-boot:run"
}

Write-Host "All services initiated!" -ForegroundColor Yellow
