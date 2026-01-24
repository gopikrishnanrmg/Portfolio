import json
from unittest.mock import AsyncMock, patch

import pytest
from services.rabbitmq.rabbitmq import publish_message


@pytest.mark.asyncio
@patch("services.rabbitmq.rabbitmq.aio_pika.connect_robust", new_callable=AsyncMock)
async def test_publish_message(mock_connect):
    mock_connection = AsyncMock()
    mock_connect.return_value = mock_connection

    mock_channel = AsyncMock()
    mock_connection.channel.return_value = mock_channel

    mock_exchange = AsyncMock()
    mock_channel.declare_exchange.return_value = mock_exchange

    routing_key = "test.key"
    payload = {"hello": "world"}

    await publish_message(routing_key, payload)

    published_message = mock_exchange.publish.call_args[0][0]
    published_routing_key = mock_exchange.publish.call_args[1]["routing_key"]

    assert published_routing_key == routing_key
    assert json.loads(published_message.body.decode("utf-8")) == payload

    mock_connect.assert_awaited_once()

    mock_connection.channel.assert_awaited_once()

    mock_channel.declare_exchange.assert_awaited_once()

    mock_exchange.publish.assert_awaited_once()
