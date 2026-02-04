from unittest.mock import AsyncMock, patch

from fastapi.testclient import TestClient

from app.main import app

client = TestClient(app)

@patch("app.routes.rabbitmq.publish_message", new_callable=AsyncMock)
def test_send_message_success(mock_publish_message):
    mock_publish_message.return_value = {"status": "published"}
    response = client.post("/api/v1/mcp", json={"session_id": "session_1", "title": "title1", "contact": "email", "message": "message1", "timestamp": 1})
    assert response.status_code == 200

    assert response.json() == {"status": "published"}
    mock_publish_message.assert_awaited_once()




