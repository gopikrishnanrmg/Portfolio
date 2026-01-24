from unittest.mock import patch, MagicMock

from fastapi.testclient import TestClient

from main import app


@patch("routes.chat_controller.generate_reply")
def test_chat_sets_cookie_when_missing(mock_reply):
    mock_reply.return_value = ("mock reply", "mock-session-id")

    client = TestClient(app)
    response = client.post("/api/v1/chat", json={"content": "hello"})

    assert response.cookies.get("session_id") == "mock-session-id"


@patch("routes.chat_controller.generate_reply")
def test_chat_does_not_override_existing_cookie(mock_reply):
    mock_reply.return_value = ("mock reply", "mock-session-id")

    client = TestClient(app, cookies={"session_id": "existing-session-id"})
    response = client.post("/api/v1/chat", json={"content": "hello"})

    assert response.cookies.get("session_id") is None
