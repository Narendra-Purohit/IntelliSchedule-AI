from fastapi import FastAPI

app = FastAPI(
    title="IntelliSchedule Document Intelligence",
    description="AI-powered academic document processing service",
    version="1.0.0"
)


@app.get("/api/v1/health")
async def health_check():
    return {
        "service": "document-intelligence",
        "status": "UP"
    }