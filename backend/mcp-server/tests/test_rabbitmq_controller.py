from unittest.mock import AsyncMock, patch

from fastapi.testclient import TestClient

from main import app

client = TestClient(app)

@patch("routes.rabbitmq.publish_message", new_callable=AsyncMock)
def test_send_message_success(mock_publish_message):
    mock_publish_message.return_value = "{ \"status\": \"published\" }"
    response = client.post("/mcp", json={"session_id": "session_1", "title": "title1", "message": "message1", "timestamp": 1})
    assert response.status_code == 200

    assert response.json() == {"status": "published"}
    mock_publish_message.assert_awaited_once()




