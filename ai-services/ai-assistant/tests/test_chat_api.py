"""Unit test for chat API endpoint."""
from fastapi.testclient import TestClient
from main import app

client = TestClient(app)

def test_health():
    res = client.get("/api/v1/health")
    assert res.status_code == 200
    assert res.json()["service"] == "ai-assistant"

def test_chat_response():
    res = client.post("/api/v1/chat", json={"message": "What is my first class on Monday?"})
    assert res.status_code == 200
    data = res.json()
    assert "reply" in data
    assert len(data["suggested_actions"]) > 0
