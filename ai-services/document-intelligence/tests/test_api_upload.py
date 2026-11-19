"""API endpoint integration tests for document upload."""
from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_health_check_endpoint():
    response = client.get("/api/v1/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "UP"
    assert data["service"] == "document-intelligence"

def test_upload_invalid_content_type():
    files = {"file": ("test.txt", b"plain text content", "text/plain")}
    response = client.post("/api/v1/documents/upload", files=files)
    assert response.status_code == 400
    assert "Only PDF files are allowed" in response.json()["detail"]
