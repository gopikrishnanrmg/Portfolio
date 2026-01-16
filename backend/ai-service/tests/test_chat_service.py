from unittest.mock import MagicMock, patch

import pytest

from dtos.chat_dtos import ChatRequest
from services.chat_service import generate_reply


@patch("services.chat_service.get_db")
@patch("services.chat_service.get_session")
@patch("services.chat_service.create_session")
@patch("services.chat_service.get_history")
@patch("services.chat_service.save_message")
@patch("services.chat_service.find_k_matches")
@patch("services.chat_service.llm")
@pytest.mark.asyncio
async def test_generate_reply_new_session(
    mock_llm,
    mock_find_k_matches,
    mock_save_message,
    mock_get_history,
    mock_create_session,
    mock_get_session,
    mock_get_db,
):
    mock_db = MagicMock()
    mock_get_db.return_value = mock_db
    mock_find_k_matches.return_value = [MagicMock(page_content="context1"), MagicMock(page_content="context2")]

    mock_get_session.return_value = None

    mock_llm.invoke.return_value = MagicMock(content="hello back!")

    class FakeRequest:
        def __init__(self):
            self.cookies = {}

    body = ChatRequest(content="hi")

    reply, session_id = await generate_reply(FakeRequest(), body) # type: ignore[arg-type]

    assert reply == "hello back!"
    assert isinstance(session_id, str)

    mock_create_session.assert_called_once()
    mock_save_message.assert_any_call(mock_db, session_id, "user", "hi")
    mock_save_message.assert_any_call(mock_db, session_id, "assistant", "hello back!")

    messages = mock_llm.invoke.call_args[0][0]
    assert messages[-1] == ("user", "hi")
    assert messages[0][0] == "system"
    assert "context1" in messages[0][1]
    assert "context2" in messages[0][1]

    assert mock_get_history.call_count == 0


@patch("services.chat_service.get_db")
@patch("services.chat_service.get_session")
@patch("services.chat_service.create_session")
@patch("services.chat_service.get_history")
@patch("services.chat_service.save_message")
@patch("services.chat_service.find_k_matches")
@patch("services.chat_service.llm")
@pytest.mark.asyncio
async def test_generate_reply_existing_session(
    mock_llm,
    mock_find_k_matches,
    mock_save_message,
    mock_get_history,
    mock_create_session,
    mock_get_session,
    mock_get_db,
):
    mock_db = MagicMock()
    mock_get_db.return_value = mock_db
    mock_find_k_matches.return_value = [MagicMock(page_content="context1"), MagicMock(page_content="context2")]

    mock_get_session.return_value = "7c84c013-4e1a-4f3d-8418-63fd7b0ceafb"

    class FakeMsg:
        def __init__(self, role, content):
            self.role = role
            self.content = content

    mock_get_history.return_value = [FakeMsg("user", "Hi"), FakeMsg("assistant", "Hi there!")]

    mock_llm.invoke.return_value = MagicMock(content="hello back!")

    class FakeRequest:
        def __init__(self):
            self.cookies = {"session_id": "7c84c013-4e1a-4f3d-8418-63fd7b0ceafb"}

    body = ChatRequest(content="hi")

    reply, session_id = await generate_reply(FakeRequest(), body) # type: ignore[arg-type]

    assert reply == "hello back!"
    assert session_id == "7c84c013-4e1a-4f3d-8418-63fd7b0ceafb"

    mock_create_session.assert_not_called()

    mock_get_history.assert_called_once_with(mock_db, session_id)
    mock_save_message.assert_any_call(mock_db, session_id, "user", "hi")
    mock_save_message.assert_any_call(mock_db, session_id, "assistant", "hello back!")

    messages = mock_llm.invoke.call_args[0][0]

    assert ("user", "Hi") in messages
    assert ("assistant", "Hi there!") in messages

    assert messages[-1] == ("user", "hi")
    assert messages[0][0] == "system"
    assert "context1" in messages[0][1]
    assert "context2" in messages[0][1]

