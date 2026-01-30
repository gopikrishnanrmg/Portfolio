from unittest.mock import patch
import pytest

from fastapi.testclient import TestClient
from starlette.status import HTTP_201_CREATED, HTTP_200_OK, HTTP_204_NO_CONTENT, HTTP_401_UNAUTHORIZED

from app.config.settings import get_settings
from app.main import app

client = TestClient(app)

@pytest.fixture(autouse=True)
def set_admin_key(monkeypatch):
    monkeypatch.setenv("ADMIN_API_KEY", "testkey")

@patch("app.routes.context_controller.add_context_item")
def test_add_context_item(mock_add):
    mock_add.return_value = 3

    response = client.post("/api/v1/context", json={"title": "doc1", "content": "content1"}, headers={"X-API-Key": get_settings().ADMIN_API_KEY})

    mock_add.assert_called_once()

    called_arg = mock_add.call_args[0][0]

    assert called_arg.title == "doc1"
    assert called_arg.content == "content1"

    assert response.status_code == HTTP_201_CREATED

@patch("app.routes.context_controller.find_k_matches")
def test_get_context_items(mock_find):
    mock_find.return_value = [
        {
            "id": "1",
            "metadata": {"title": "Doc1"},
            "page_content": "content1",
        },
        {
            "id": "2",
            "metadata": {"title": "Doc2"},
            "page_content": "content2",
        },
    ]

    response = client.get("/api/v1/context", params={"query": "what is the best match?", "k": 2}, headers={"X-API-Key": get_settings().ADMIN_API_KEY})

    mock_find.assert_called_once_with("what is the best match?", 2)
    assert response.status_code == HTTP_200_OK

@patch("app.routes.context_controller.soft_delete_by_title")
def test_delete_context_item(mock_delete):
    response = client.delete("/api/v1/context", params={"title": "doc1"}, headers={"X-API-Key": get_settings().ADMIN_API_KEY})

    mock_delete.assert_called_once_with("doc1")
    assert response.status_code == HTTP_204_NO_CONTENT

def test_missing_api_key_post():
    response = client.post("/api/v1/context", json={"title": "doc1", "content": "c"})
    assert response.status_code == HTTP_401_UNAUTHORIZED

def test_missing_api_key_get():
    response = client.get("/api/v1/context", params={"query": "x", "k": 1})
    assert response.status_code == HTTP_401_UNAUTHORIZED

def test_missing_api_key_delete():
    response = client.delete("/api/v1/context", params={"title": "doc1"})
    assert response.status_code == HTTP_401_UNAUTHORIZED

def test_wrong_api_key_post():
    response = client.post("/api/v1/context", json={"title": "doc1", "content": "c"}, headers={"X-API-Key": "wrong"})
    assert response.status_code == HTTP_401_UNAUTHORIZED

def test_wrong_api_key_get():
    response = client.get("/api/v1/context", params={"query": "x", "k": 1}, headers={"X-API-Key": "wrong"})
    assert response.status_code == HTTP_401_UNAUTHORIZED

def test_wrong_api_key_delete():
    response = client.delete("/api/v1/context", params={"title": "doc1"}, headers={"X-API-Key": "wrong"})
    assert response.status_code == HTTP_401_UNAUTHORIZED
